package com.girfalco.driverapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import android.view.View
import android.view.WindowManager

class SplashScreenActivity : AppCompatActivity() {
    
    private val SPLASH_DELAY = 3000L // 3 seconds
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Set window flags BEFORE setContentView
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        
        // Enable edge-to-edge and hide system bars
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
        
        setContentView(R.layout.splash_screen)
        
        // Load SVG logo from assets
        val webView = findViewById<WebView>(R.id.logo_webview)
        webView.settings.javaScriptEnabled = true
        webView.setBackgroundColor(0) // Transparent background
        
        try {
            val inputStream = assets.open("splash_screen_logo.svg")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val svgContent = reader.readText()
            reader.close()
            
            val htmlContent = """
                <html>
                <head>
                    <style>
                        body { margin: 0; padding: 0; background: transparent; }
                        svg { width: 100%; height: 100%; }
                    </style>
                </head>
                <body>
                    $svgContent
                </body>
                </html>
            """.trimIndent()
            
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        // Delay and then move to RouteHistoryActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_DELAY)
    }
    
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }
    }
}