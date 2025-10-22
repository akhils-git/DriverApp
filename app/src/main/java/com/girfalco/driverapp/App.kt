package com.girfalco.driverapp

import android.app.Application
import android.content.Context
import com.girfalco.driverapp.network.AuthTokenStore

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Restore persisted token (if any) so in-memory AuthTokenStore has it on app start
        val prefs = getSharedPreferences("girfalco_prefs", Context.MODE_PRIVATE)
        AuthTokenStore.token = prefs.getString("auth_token", null)
    }
}

