package com.ps5updateblocker

import android.util.Log
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets

object ProxyService {
    private const val TAG = "ProxyService"
    const val PORT = 8080
    
    private var serverSocket: ServerSocket? = null
    private val isRunning = AtomicBoolean(false)
    private var proxyThread: Thread? = null

    fun start() {
        if (isRunning.get()) return

        try {
            serverSocket = ServerSocket(PORT)
            isRunning.set(true)
            
            proxyThread = thread(start = true) {
                Log.d(TAG, "Proxy server started on port $PORT")
                while (isRunning.get()) {
                    try {
                        val clientSocket = serverSocket?.accept() ?: break
                        thread { handleClient(clientSocket) }
                    } catch (e: Exception) {
                        if (isRunning.get()) Log.e(TAG, "Accept error: ${e.message}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Start error: ${e.message}")
            stop()
        }
    }

    fun stop() {
        isRunning.set(false)
        try {
            serverSocket?.close()
        } catch (e: Exception) {
            // Ignored
        }
        proxyThread?.interrupt()
        serverSocket = null
        Log.d(TAG, "Proxy server stopped")
    }
    
    fun isRunning(): Boolean = isRunning.get()

    private fun handleClient(clientSocket: Socket) {
        try {
            val input = clientSocket.getInputStream()
            val output = clientSocket.getOutputStream()
            
            // Read initial request line
            val buffer = ByteArray(8192)
            val bytesRead = input.read(buffer)
            if (bytesRead == -1) {
                clientSocket.close()
                return
            }
            
            val request = String(buffer, 0, bytesRead, StandardCharsets.ISO_8859_1)
            val lines = request.split("\r\n")
            if (lines.isEmpty()) {
                clientSocket.close()
                return
            }
            
            val requestLine = lines[0]
            val parts = requestLine.split(" ")
            if (parts.size < 2) {
                clientSocket.close()
                return
            }
            
            val method = parts[0]
            val url = parts[1]
            
            // Extract host
            val host = if (method == "CONNECT") {
                url.split(":")[0]
            } else {
                // For HTTP GET, extract hostname from URL or Host header
                extractHost(url, lines)
            }
            
            // Check if blocked
            if (host != null && BlockedDomains.isBlocked(host)) {
                Log.d(TAG, "BLOCKED: $host")
                if (method == "CONNECT") {
                    // Send 403 Forbidden or just close connection
                    val response = "HTTP/1.1 403 Forbidden\r\n\r\n"
                    output.write(response.toByteArray())
                } else {
                    // Send simple HTML block page
                    val body = "<html><body><h1>Blocked by PS5 Update Blocker</h1></body></html>"
                    val response = "HTTP/1.1 403 Forbidden\r\nContent-Type: text/html\r\nContent-Length: ${body.length}\r\n\r\n$body"
                    output.write(response.toByteArray())
                }
                output.flush()
                clientSocket.close()
                return
            }
            
            Log.d(TAG, "Allowing: $host Method: $method")
            
            if (method == "CONNECT") {
                handleConnect(clientSocket, url)
            } else {
                handleHttp(clientSocket, host, request.toByteArray(StandardCharsets.ISO_8859_1))
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Client error: ${e.message}")
            try { clientSocket.close() } catch (e2: Exception) {}
        }
    }
    
    private fun extractHost(url: String, lines: List<String>): String? {
        // Try getting from URL first (http://example.com/...)
        if (url.startsWith("http://")) {
            val noProtocol = url.substring(7)
            val slashIndex = noProtocol.indexOf('/')
            return if (slashIndex != -1) noProtocol.substring(0, slashIndex) else noProtocol
        }
        
        // Try getting from Host header
        for (line in lines) {
            if (line.startsWith("Host: ", ignoreCase = true)) {
                return line.substring(6).trim()
            }
        }
        return null
    }

    private fun handleConnect(clientSocket: Socket, url: String) {
        val hostParts = url.split(":")
        val host = hostParts[0]
        val port = if (hostParts.size > 1) hostParts[1].toInt() else 443
        
        try {
            val serverSocket = Socket(host, port)
            
            // Send 200 Connection Established to client
            clientSocket.getOutputStream().write("HTTP/1.1 200 Connection Established\r\n\r\n".toByteArray())
            clientSocket.getOutputStream().flush()
            
            // Start tunneling
            val clientToRemote = thread { transfer(clientSocket.getInputStream(), serverSocket.getOutputStream()) }
            val remoteToClient = thread { transfer(serverSocket.getInputStream(), clientSocket.getOutputStream()) }
            
            clientToRemote.join()
            remoteToClient.join()
            
            serverSocket.close()
            clientSocket.close()
        } catch (e: Exception) {
            Log.e(TAG, "Tunnel check error: $host $port ${e.message}")
            clientSocket.close()
        }
    }
    
    private fun handleHttp(clientSocket: Socket, host: String?, initialData: ByteArray) {
        if (host == null) {
            clientSocket.close()
            return
        }
        
        try {
            val port = 80 // Default HTTP port
            val serverSocket = Socket(host, port)
            
            // Send initial request to server
            serverSocket.getOutputStream().write(initialData)
            serverSocket.getOutputStream().flush()
            
            // Tunnel rest of the traffic
            val clientToRemote = thread { transfer(clientSocket.getInputStream(), serverSocket.getOutputStream()) }
            val remoteToClient = thread { transfer(serverSocket.getInputStream(), clientSocket.getOutputStream()) }
            
            clientToRemote.join()
            remoteToClient.join()
            
            serverSocket.close()
            clientSocket.close()
        } catch (e: Exception) {
             Log.e(TAG, "HTTP proxy error: ${e.message}")
             clientSocket.close()
        }
    }
    
    private fun transfer(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(8192)
        try {
            var bytesRead: Int
            while (input.read(buffer).also { bytesRead = it } != -1) {
                output.write(buffer, 0, bytesRead)
                output.flush()
            }
        } catch (e: Exception) {
            // Connection closed
        }
    }
}
