package com.ps5updateblocker

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.Collections

class MainActivity : Activity() {

    companion object {
        const val TAG = "MainActivity"
        const val ACTION_SERVICE_STATUS = "com.ps5updateblocker.SERVICE_STATUS"
    }

    private lateinit var statusIcon: ImageView
    private lateinit var statusText: TextView
    private lateinit var proxyInfoText: TextView
    private lateinit var blockedCountText: TextView
    private lateinit var domainListText: TextView
    private lateinit var toggleButton: Button

    private val serviceStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateUI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupListeners()
        updateUI()
        displayDomainList()

        // Bildirim izni kontrolü (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            registerReceiver(serviceStatusReceiver, IntentFilter(ACTION_SERVICE_STATUS), Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(serviceStatusReceiver, IntentFilter(ACTION_SERVICE_STATUS))
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(serviceStatusReceiver)
    }

    private fun initViews() {
        statusIcon = findViewById(R.id.statusIcon)
        statusText = findViewById(R.id.statusText)
        proxyInfoText = findViewById(R.id.proxyInfoText)
        blockedCountText = findViewById(R.id.blockedCountText)
        domainListText = findViewById(R.id.domainListText)
        toggleButton = findViewById(R.id.toggleButton)
    }

    private fun setupListeners() {
        toggleButton.setOnClickListener {
            if (ProxyService.isRunning()) {
                stopProxyService()
            } else {
                startProxyService()
            }
        }
    }

    private fun displayDomainList() {
        domainListText.text = BlockedDomains.getFormattedList()
        blockedCountText.text = getString(
            R.string.blocked_domains_count,
            BlockedDomains.getCount()
        )
    }

    private fun getDeviceIpAddress(): String {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress && addr is Inet4Address) {
                        return addr.hostAddress ?: ""
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "Bilinmiyor"
    }

    private fun updateUI() {
        val isRunning = ProxyService.isRunning()
        
        if (isRunning) {
            statusIcon.setImageResource(R.drawable.ic_block_active)
            statusText.text = getString(R.string.status_active)
            statusText.setTextColor(Color.parseColor("#4CAF50"))
            
            val ip = getDeviceIpAddress()
            proxyInfoText.text = "Proxy Çalışıyor\nIP: $ip\nPort: ${ProxyService.PORT}\n\nLütfen PS5 ağ ayarlarında bu Proxy bilgilerini girin."
            
            toggleButton.text = getString(R.string.stop_blocking)
            toggleButton.setBackgroundColor(Color.parseColor("#F44336"))
        } else {
            statusIcon.setImageResource(R.drawable.ic_block)
            statusText.text = getString(R.string.status_inactive)
            statusText.setTextColor(Color.parseColor("#F44336"))
            
            proxyInfoText.text = "Proxy Durduruldu"
            
            toggleButton.text = getString(R.string.start_blocking)
            toggleButton.setBackgroundColor(Color.parseColor("#4CAF50"))
        }
    }

    private fun startProxyService() {
        val intent = Intent(this, ProxyForegroundService::class.java).apply {
            action = ProxyForegroundService.ACTION_START
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        
        // UI güncellemesi için kısa gecikme
        toggleButton.postDelayed({ updateUI() }, 500)
        
        Toast.makeText(this, "Proxy başlatıldı", Toast.LENGTH_SHORT).show()
    }

    private fun stopProxyService() {
        val intent = Intent(this, ProxyForegroundService::class.java).apply {
            action = ProxyForegroundService.ACTION_STOP
        }
        startService(intent)
        
        // UI güncellemesi için kısa gecikme
        toggleButton.postDelayed({ updateUI() }, 500)
        
        Toast.makeText(this, "Proxy durduruldu", Toast.LENGTH_SHORT).show()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!areNotificationsEnabled()) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }
                startActivity(intent)
            }
        }
    }

    private fun areNotificationsEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            val channel = notificationManager.getNotificationChannel(ProxyForegroundService.CHANNEL_ID)
            channel?.importance != android.app.NotificationManager.IMPORTANCE_NONE
        } else {
            true
        }
    }
}
