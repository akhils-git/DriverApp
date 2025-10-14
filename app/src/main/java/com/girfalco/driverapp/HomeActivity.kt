package com.girfalco.driverapp

import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        // Hide the navigation bar and status bar for immersive fullscreen experience
        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
            }
        }
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        )

        // Set status bar color to white to match user card
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = android.graphics.Color.WHITE
            window.navigationBarColor = android.graphics.Color.parseColor("#FFF9E5")
        }

        // Hide bottom navigation bar (immersive sticky mode)
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
        )

        // Find the filter icon ImageView
        val filterIcon = findViewById<ImageView>(R.id.home_screen_filter)
        filterIcon?.setOnClickListener {
            showFilterPopup()
        }

        // Add scale animation to View Details button (first instance only)
        val currentLocationDetails1 = findViewById<View>(R.id.current_location_details_include_1)
        val viewDetailsButtonRow1 = currentLocationDetails1?.findViewById<View>(R.id.view_details_button_row)
        viewDetailsButtonRow1?.setOnClickListener { v ->
            v.animate()
                .scaleX(0.93f)
                .scaleY(0.93f)
                .setDuration(80)
                .withEndAction {
                    v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(80)
                        .start()
                }
                .start()
        }
    }

    private fun showFilterPopup() {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.filter_popup, null)
        dialog.setContentView(view)

        // Hide navigation bar when popup is shown (immersive flags)
        val originalUiFlags = window.decorView.systemUiVisibility
        dialog.setOnShowListener {
            val flags = (
                android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
            )
            window.decorView.systemUiVisibility = flags
            dialog.window?.decorView?.systemUiVisibility = flags
            dialog.window?.setFlags(
                android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            // Explicitly set the bottom sheet background to transparent
            val bottomSheet = dialog.delegate.findViewById<android.view.View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundColor(android.graphics.Color.TRANSPARENT)
        }
        dialog.setOnDismissListener {
            window.decorView.systemUiVisibility = originalUiFlags
        }

        // Make the nav bar transparent
        val dialogWindow = dialog.window
        if (dialogWindow != null) {
            dialogWindow.navigationBarColor = android.graphics.Color.TRANSPARENT
            dialogWindow.setBackgroundDrawableResource(android.R.color.transparent)
        }

        // --- Custom circle checkbox logic (like login screen Remind Me) ---
        // Find the included view for the custom checkbox row
        val includedRow = view.findViewById<View>(R.id.filter_option_all)
        val circleCheckbox = includedRow?.findViewById<ImageView>(R.id.popup_action_checkbox)
        val textView = includedRow?.findViewById<View>(R.id.popup_action_text)
        var isChecked = true
        fun updateCircleCheckbox() {
            if (circleCheckbox != null) {
                if (isChecked) {
                    circleCheckbox.setImageResource(R.drawable.checkbox_checked)
                } else {
                    circleCheckbox.setImageResource(R.drawable.checkbox_unchecked)
                }
            }
        }
        updateCircleCheckbox()
        circleCheckbox?.setOnClickListener {
            isChecked = !isChecked
            updateCircleCheckbox()
        }
        // Also allow tapping the text to toggle
        textView?.setOnClickListener {
            isChecked = !isChecked
            updateCircleCheckbox()
        }

        // Set Apply button to dismiss the dialog
        view.findViewById<Button>(R.id.apply_button)?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}