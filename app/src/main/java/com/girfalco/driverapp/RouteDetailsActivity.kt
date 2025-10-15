package com.girfalco.driverapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class RouteDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_details)

        // Hide status and navigation bars for immersive experience
        window.decorView.post {
            window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                        or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            )
        }

        // Back button logic with tap animation
        val backButton = findViewById<android.widget.ImageView>(R.id.route_details_back_arrow)
        backButton.setOnClickListener {
            backButton.animate().scaleX(0.85f).scaleY(0.85f).setDuration(80).withEndAction {
                backButton.animate().scaleX(1f).scaleY(1f).setDuration(80).withEndAction {
                    val intent = android.content.Intent(this, HomeActivity::class.java)
                    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }.start()
            }.start()
        }
    }
}
