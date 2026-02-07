package com.ps5updateblocker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    
    companion object {
        const val TAG = "BootReceiver"
        const val PREFS_NAME = "PS5UpdateBlockerPrefs"
        const val KEY_AUTO_START = "auto_start"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(TAG, "Boot completed received")
            
            // Otomatik başlatma ayarını kontrol et
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val autoStart = prefs.getBoolean(KEY_AUTO_START, false)
            
            if (autoStart) {
                Log.d(TAG, "Auto-starting DNS blocker service")
                startService(context)
            }
        }
    }

    private fun startService(context: Context) {
        val serviceIntent = Intent(context, ProxyForegroundService::class.java).apply {
            action = ProxyForegroundService.ACTION_START
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}
