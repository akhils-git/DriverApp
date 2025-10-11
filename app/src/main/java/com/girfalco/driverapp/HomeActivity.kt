package com.girfalco.driverapp

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Use normal layout with status bar visible
        // This allows proper margin calculation from status bar
        setContentView(R.layout.activity_home)
    }
}