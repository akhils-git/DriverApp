package com.girfalco.driverapp

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import com.girfalco.driverapp.databinding.ActivityChangePasswordBinding
import java.io.IOException

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set full-screen flags (immersive mode)
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

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load SVGs for background and logo (with fallback)
        loadSvgIntoImageView(findViewById(R.id.change_password_background_image), "login_screen_background.svg")
        loadSvgIntoImageView(findViewById(R.id.change_password_logo), "login_screen_logo.svg")

        // Floating label behavior for password fields
        setupFloatingLabel(
            findViewById(R.id.current_password_input),
            findViewById(R.id.current_password_floating_label),
            "Current Password"
        )
        setupFloatingLabel(
            findViewById(R.id.new_password_input),
            findViewById(R.id.new_password_floating_label),
            "New Password"
        )
        setupFloatingLabel(
            findViewById(R.id.confirm_new_password_input),
            findViewById(R.id.confirm_new_password_floating_label),
            "Confirm New Password"
        )

        // Back button: tap animation and finish
        val backButton = findViewById<ImageView>(R.id.change_password_back_button)
        backButton.setOnClickListener {
            backButton.animate().scaleX(0.85f).scaleY(0.85f).setDuration(80).withEndAction {
                backButton.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
                finish()
            }.start()
        }

        // Change Password button logic (placeholder)
        val changePasswordButton = findViewById<android.view.View>(R.id.change_password_button)
        changePasswordButton.setOnClickListener {
            // Handle change password
        }
    }

    private fun setupFloatingLabel(input: android.widget.EditText, label: android.widget.TextView, hint: String) {
        input.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                if (s?.isNotEmpty() == true) {
                    showFloatingLabel(label)
                    input.hint = ""
                    input.gravity = android.view.Gravity.START or android.view.Gravity.TOP
                } else {
                    hideFloatingLabel(label)
                    input.hint = hint
                    input.gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                }
            }
        })
    }

    private fun showFloatingLabel(label: android.widget.TextView) {
        if (label.visibility == View.GONE) {
            label.visibility = View.VISIBLE
            label.alpha = 0f
            label.translationY = 20f
            label.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(200)
                .start()
        }
    }

    private fun hideFloatingLabel(label: android.widget.TextView) {
        if (label.visibility == View.VISIBLE) {
            label.animate()
                .alpha(0f)
                .translationY(20f)
                .setDuration(200)
                .withEndAction {
                    label.visibility = View.GONE
                    label.translationY = 0f
                }
                .start()
        }
    }

    private fun loadSvgIntoImageView(imageView: ImageView, fileName: String) {
        try {
            val inputStream = assets.open(fileName)
            val svg = SVG.getFromInputStream(inputStream)

            // Get appropriate dimensions based on file
            val width = when (fileName) {
                "login_screen_logo.svg" -> 150
                else -> 1000
            }
            val height = when (fileName) {
                "login_screen_logo.svg" -> 150
                else -> 1000
            }

            // Create bitmap from SVG
            val bitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(bitmap)

            // Set SVG document size and render
            svg.documentWidth = width.toFloat()
            svg.documentHeight = height.toFloat()
            svg.renderToCanvas(canvas)

            // Set the bitmap to ImageView
            imageView.setImageBitmap(bitmap)

        } catch (e: IOException) {
            e.printStackTrace()
            setFallbackImages(imageView, fileName)
        } catch (e: SVGParseException) {
            e.printStackTrace()
            setFallbackImages(imageView, fileName)
        }
    }

    private fun setFallbackImages(imageView: ImageView, fileName: String) {
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
