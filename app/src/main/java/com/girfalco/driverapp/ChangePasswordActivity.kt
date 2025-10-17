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
import android.content.Intent

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

    // Set all icons to gray on screen load using view binding only
    binding.passwordRequirementIconLeast8Chars.setImageResource(R.drawable.changepassword_screen_gry_round)
    binding.passwordRequirementIconRequirements.setImageResource(R.drawable.changepassword_screen_gry_round)
    binding.passwordRequirementIconNotSameOldPassword.setImageResource(R.drawable.changepassword_screen_gry_round)

        // Real-time password validation logic
        binding.passwordRequirementIconLeast8Chars.setImageResource(R.drawable.changepassword_screen_gry_round)
        binding.newPasswordInput.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                val password = s?.toString() ?: ""

                // Validation for at least 8 characters
                if (password.length >= 8) {
                    binding.passwordRequirementIconLeast8Chars.setImageResource(R.drawable.changepassword_screen_green_round)
                } else {
                    binding.passwordRequirementIconLeast8Chars.setImageResource(R.drawable.changepassword_screen_gry_round)
                }

                // Validation for at least one uppercase letter, one number, and one special character
                val regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*]).+$".toRegex()
                if (regex.containsMatchIn(password)) {
                    binding.passwordRequirementIconRequirements.setImageResource(R.drawable.changepassword_screen_green_round)
                } else {
                    binding.passwordRequirementIconRequirements.setImageResource(R.drawable.changepassword_screen_gry_round)
                }
            }
        })

        // Validation for Confirm New Password
        binding.confirmNewPasswordInput.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                val confirmPassword = s?.toString() ?: ""
                val currentPassword = binding.currentPasswordInput.text?.toString() ?: ""

                // Validation: Confirm New Password is different from Current Password
                if (confirmPassword.isNotEmpty() && confirmPassword != currentPassword) {
                    binding.passwordRequirementIconNotSameOldPassword.setImageResource(R.drawable.changepassword_screen_green_round)
                } else {
                    binding.passwordRequirementIconNotSameOldPassword.setImageResource(R.drawable.changepassword_screen_gry_round)
                }
            }
        })


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

        // Load SVGs for background and logo (with fallback)
        loadSvgIntoImageView(binding.changePasswordBackgroundImage, "login_screen_background.svg")
        loadSvgIntoImageView(binding.changePasswordLogo, "login_screen_logo.svg")

        // Floating label behavior for password fields (use binding views)
        setupFloatingLabel(
            binding.currentPasswordInput,
            binding.currentPasswordFloatingLabel,
            "Current Password"
        )
        setupFloatingLabel(
            binding.newPasswordInput,
            binding.newPasswordFloatingLabel,
            "New Password"
        )
        setupFloatingLabel(
            binding.confirmNewPasswordInput,
            binding.confirmNewPasswordFloatingLabel,
            "Confirm New Password"
        )

        // Back button: tap animation and finish (binding)
        val backButton = binding.changePasswordBackButton
        backButton.setOnClickListener {
            backButton.animate().scaleX(0.85f).scaleY(0.85f).setDuration(80).withEndAction {
                backButton.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
                finish()
            }.start()
        }

        // Change Password button logic (placeholder)
        binding.changePasswordButton.setOnClickListener {
            // Navigate to Password Updated Successfully screen
            val intent = Intent(this, PasswordUpdatedActivity::class.java)
            startActivity(intent)
            finish()
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
