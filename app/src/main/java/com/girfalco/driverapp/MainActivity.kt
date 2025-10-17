package com.girfalco.driverapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import androidx.core.content.ContextCompat
import android.text.TextWatcher
import android.text.Editable
import android.view.Gravity
import java.io.IOException

class MainActivity : AppCompatActivity() {
    
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var emailFloatingLabel: TextView
    private lateinit var passwordFloatingLabel: TextView
    private lateinit var rememberCheckboxIcon: ImageView
    private lateinit var rememberCheckboxText: TextView
    private lateinit var loginButton: MaterialButton
    private lateinit var faceIdContainer: View
    private lateinit var faceIdIcon: ImageView
    private lateinit var forgotPasswordText: TextView
    private lateinit var backgroundImage: ImageView
    private lateinit var loginLogo: ImageView
    
    // Custom checkbox state
    private var isRememberMeChecked = true
    
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
        
        setContentView(R.layout.activity_login)
        
        initializeViews()
        setupClickListeners()
    }
    
    private fun initializeViews() {
        backgroundImage = findViewById(R.id.background_image)
        loginLogo = findViewById(R.id.login_logo)
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        emailInputLayout = findViewById(R.id.email_input_layout)
        passwordInputLayout = findViewById(R.id.password_input_layout)
        emailFloatingLabel = findViewById(R.id.email_floating_label)
        passwordFloatingLabel = findViewById(R.id.password_floating_label)
        rememberCheckboxIcon = findViewById(R.id.remember_checkbox_icon)
        rememberCheckboxText = findViewById(R.id.remember_checkbox_text)
        loginButton = findViewById(R.id.login_button)
        faceIdContainer = findViewById(R.id.face_id_container)
        faceIdIcon = findViewById(R.id.face_id_icon)
        forgotPasswordText = findViewById(R.id.forgot_password)
        
        // Load SVG files
        loadSvgIntoImageView(backgroundImage, "login_screen_background.svg")
        loadSvgIntoImageView(loginLogo, "login_screen_logo.svg")
        loadSvgIntoImageView(faceIdIcon, "login_screen_biometric.svg")
        
        // Add underline to "Forgot Password?" text
        forgotPasswordText.paintFlags = forgotPasswordText.paintFlags or android.graphics.Paint.UNDERLINE_TEXT_FLAG
        
        // Setup focus listeners for border color changes
        setupInputFocusListeners()
        
        // Setup custom floating labels
        setupCustomFloatingLabels()
        
        // Setup custom checkbox
        setupCustomCheckbox()
    }
    
    private fun setupClickListeners() {
        loginButton.setOnClickListener {
            handleLogin()
        }
        
        faceIdContainer.setOnClickListener {
            handleFaceIdLogin()
        }
        
        forgotPasswordText.setOnClickListener {
            forgotPasswordText.animate().scaleX(0.85f).scaleY(0.85f).setDuration(80).withEndAction {
                forgotPasswordText.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
                handleForgotPassword()
            }.start()
        }
        
        // Custom checkbox click listeners
        rememberCheckboxIcon.setOnClickListener {
            toggleRememberMe()
        }
        
        rememberCheckboxText.setOnClickListener {
            toggleRememberMe()
        }
    }
    
    private fun handleLogin() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        
        // Skip validation for now - accept any input
        // TODO: Implement actual authentication logic with API integration
        
        // Show success message
        Toast.makeText(this, "Login successful! Welcome to Girfalco Driver App", Toast.LENGTH_SHORT).show()
        
        // Save remember me preference if checked
        if (isRememberMeChecked) {
            // TODO: Save user credentials securely
            Toast.makeText(this, "Login details will be remembered", Toast.LENGTH_SHORT).show()
        }
        
        // Navigate to Home Activity
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // Close login screen
    }
    
    private fun handleFaceIdLogin() {
        // TODO: Implement biometric authentication
        Toast.makeText(this, "Face ID login coming soon!", Toast.LENGTH_SHORT).show()
    }
    
    private fun handleForgotPassword() {
        // Launch ForgotPasswordActivity
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }
    
    private fun loadSvgIntoImageView(imageView: ImageView, fileName: String) {
        try {
            val inputStream = assets.open(fileName)
            val svg = SVG.getFromInputStream(inputStream)
            
            // Get appropriate dimensions based on file
            val width = when (fileName) {
                "login_screen_logo.svg" -> 150
                "login_screen_biometric.svg" -> 50
                else -> 1000
            }
            val height = when (fileName) {
                "login_screen_logo.svg" -> 150
                "login_screen_biometric.svg" -> 50
                else -> 1000
            }
            
            // Create bitmap from SVG
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            
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
            "login_screen_biometric.svg" -> {
                imageView.setImageResource(android.R.drawable.ic_menu_camera)
            }
        }
    }
    
    private fun setupInputFocusListeners() {
        val whiteColor = ContextCompat.getColor(this, R.color.input_border_unfocused)
        val blueColor = ContextCompat.getColor(this, R.color.input_border_focused)
        
        emailInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                emailInputLayout.boxStrokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_width_focused)
                emailInputLayout.boxStrokeColor = blueColor
            } else {
                emailInputLayout.boxStrokeWidth = 0
                emailInputLayout.boxStrokeColor = whiteColor
            }
        }
        
        passwordInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                passwordInputLayout.boxStrokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_width_focused)
                passwordInputLayout.boxStrokeColor = blueColor
            } else {
                passwordInputLayout.boxStrokeWidth = 0
                passwordInputLayout.boxStrokeColor = whiteColor
            }
        }
    }
    
    private fun setupCustomFloatingLabels() {
        // Email floating label logic with animation
        emailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) {
                    // When typing: show floating label, change to top gravity
                    showFloatingLabel(emailFloatingLabel)
                    emailInput.hint = ""
                    emailInput.gravity = Gravity.START or Gravity.TOP
                } else {
                    // When empty: hide floating label, center gravity, show placeholder
                    hideFloatingLabel(emailFloatingLabel)
                    emailInput.hint = "Email Address"
                    emailInput.gravity = Gravity.START or Gravity.CENTER_VERTICAL
                }
            }
        })
        
        // Password floating label logic with animation
        passwordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) {
                    // When typing: show floating label, change to top gravity
                    showFloatingLabel(passwordFloatingLabel)
                    passwordInput.hint = ""
                    passwordInput.gravity = Gravity.START or Gravity.TOP
                } else {
                    // When empty: hide floating label, center gravity, show placeholder
                    hideFloatingLabel(passwordFloatingLabel)
                    passwordInput.hint = "Password"
                    passwordInput.gravity = Gravity.START or Gravity.CENTER_VERTICAL
                }
            }
        })
    }
    
    private fun showFloatingLabel(label: TextView) {
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
    
    private fun hideFloatingLabel(label: TextView) {
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
    
    private fun setupCustomCheckbox() {
        // Set initial state - checked (blue background with checkmark)
        updateCheckboxAppearance()
    }
    
    private fun toggleRememberMe() {
        isRememberMeChecked = !isRememberMeChecked
        updateCheckboxAppearance()
    }
    
    private fun updateCheckboxAppearance() {
        if (isRememberMeChecked) {
            // Checked: Blue background with white checkmark
            rememberCheckboxIcon.setImageResource(R.drawable.checkbox_checked)
        } else {
            // Unchecked: White background with blue border
            rememberCheckboxIcon.setImageResource(R.drawable.checkbox_unchecked)
        }
    }
}