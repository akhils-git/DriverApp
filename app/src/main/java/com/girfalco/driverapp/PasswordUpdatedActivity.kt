package com.girfalco.driverapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.girfalco.driverapp.databinding.ActivityPasswordUpdatedBinding

class PasswordUpdatedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordUpdatedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set full-screen flags
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding = ActivityPasswordUpdatedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load SVG files
        loadSvgIntoImageView(binding.backgroundImage, "login_screen_background.svg")
        loadSvgIntoImageView(binding.loginLogo, "login_screen_logo.svg")

        // Animate the success image
        binding.passwordSuccessImage.postDelayed({
            binding.passwordSuccessImage.animate().scaleX(1.3f).scaleY(1.3f).setDuration(400).withEndAction {
                binding.passwordSuccessImage.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
            }.start()
        }, 500)

        // Go to Login button
        binding.goToLoginButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun loadSvgIntoImageView(imageView: android.widget.ImageView, fileName: String) {
        try {
            val inputStream = assets.open(fileName)
            val svg = com.caverock.androidsvg.SVG.getFromInputStream(inputStream)

            val width = when (fileName) {
                "login_screen_logo.svg" -> 150
                else -> 1000
            }
            val height = when (fileName) {
                "login_screen_logo.svg" -> 150
                else -> 1000
            }

            val bitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(bitmap)

            svg.documentWidth = width.toFloat()
            svg.documentHeight = height.toFloat()
            svg.renderToCanvas(canvas)

            imageView.setImageBitmap(bitmap)

        } catch (e: Exception) {
            e.printStackTrace()
            setFallbackImages(imageView, fileName)
        }
    }

    private fun setFallbackImages(imageView: android.widget.ImageView, fileName: String) {
        when (fileName) {
            "login_screen_background.svg" -> {
                imageView.setBackgroundResource(R.drawable.login_gradient_background)
            }
            "login_screen_logo.svg" -> {
                imageView.setImageResource(R.drawable.ic_girfalco_logo)
            }
        }
    }
}