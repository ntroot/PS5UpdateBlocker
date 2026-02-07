package com.ps5updateblocker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log

class ProxyForegroundService : Service() {

    companion object {
        const val ACTION_START = "com.ps5updateblocker.PROXY_START"
        const val ACTION_STOP = "com.ps5updateblocker.PROXY_STOP"
        const val CHANNEL_ID = "proxy_service_channel"
        const val NOTIFICATION_ID = 2
        const val TAG = "ProxyForegroundService"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startProxy()
            ACTION_STOP -> stopProxy()
        }
        return START_STICKY
    }

    private fun startProxy() {
        if (ProxyService.isRunning()) {
            Log.d(TAG, "Proxy already running")
            return
        }

        startForeground(NOTIFICATION_ID, createNotification())
        ProxyService.start()
        Log.d(TAG, "Proxy Service started")
        
        // Broadcast status update
        sendBroadcast(Intent(MainActivity.ACTION_SERVICE_STATUS))
    }

    private fun stopProxy() {
        ProxyService.stop()
        Log.d(TAG, "Proxy Service stopped")
        
        // Broadcast status update
        sendBroadcast(Intent(MainActivity.ACTION_SERVICE_STATUS))
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            stopForeground(true)
        }
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.channel_description)
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val contentText = "Proxy: ${ProxyService.PORT} | Engelleme Aktif"

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.service_running))
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_LOW)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()
        } else {
            Notification.Builder(this)
                .setContentTitle(getString(R.string.service_running))
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_LOW)
                .build()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopProxy()
    }
}
