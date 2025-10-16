package com.girfalco.driverapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class RouteDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_details)

        // Add Note Bottom Sheet logic
        val addNoteSwitchContainer = findViewById<android.widget.LinearLayout>(R.id.add_note_switch_container)
        addNoteSwitchContainer.setOnClickListener {
            val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.add_note_bottom_sheet, null)
            // Ensure popup resizes above keyboard
            dialog.window?.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            dialog.setContentView(view)
            // Match immersive and background logic from logout popup
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
                val bottomSheet = dialog.delegate.findViewById<android.view.View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.setBackgroundColor(android.graphics.Color.TRANSPARENT)
            }
            dialog.setOnDismissListener {
                window.decorView.systemUiVisibility = originalUiFlags
            }
            val dialogWindow = dialog.window
            if (dialogWindow != null) {
                dialogWindow.navigationBarColor = android.graphics.Color.TRANSPARENT
                dialogWindow.setBackgroundDrawableResource(android.R.color.transparent)
            }
            // Cancel button logic
            val cancelButton = view.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.cancel_button)
            cancelButton.setOnClickListener { dialog.dismiss() }
            // Add Note button logic (close for now, add logic as needed)
            val addNoteButton = view.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.add_note_button)
            addNoteButton.setOnClickListener { dialog.dismiss() }
            val addNoteEditText = view.findViewById<android.widget.EditText>(R.id.add_note_textbox)
            addNoteEditText?.addTextChangedListener(object : android.text.TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    addNoteButton.text = if (!s.isNullOrEmpty()) "Update" else "Add Note"
                }
                override fun afterTextChanged(s: android.text.Editable?) {}
            })
            dialog.show()
        }

        // Active Ride Bottom Sheet logic (show with hamburger menu or your trigger)
    val hamburgerMenu = findViewById<android.widget.ImageView?>(R.id.route_details_hamburger_menu)
        hamburgerMenu?.setOnClickListener {
            val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.active_ride_bottom_sheet, null)
            dialog.window?.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            dialog.setContentView(view)
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
                val bottomSheet = dialog.delegate.findViewById<android.view.View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.setBackgroundColor(android.graphics.Color.TRANSPARENT)
            }
            dialog.setOnDismissListener {
                window.decorView.systemUiVisibility = originalUiFlags
            }
            val dialogWindow = dialog.window
            if (dialogWindow != null) {
                dialogWindow.navigationBarColor = android.graphics.Color.TRANSPARENT
                dialogWindow.setBackgroundDrawableResource(android.R.color.transparent)
            }
            // Add Ride Complete button click logic
            val rideCompleteButton = view.findViewById<android.view.View>(R.id.ride_complete_button)
            rideCompleteButton?.setOnClickListener {
                val confirmDialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
                val confirmView = layoutInflater.inflate(R.layout.ride_complete_confirm_bottom_sheet, null)
                confirmDialog.window?.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                confirmDialog.setContentView(confirmView)
                val originalConfirmUiFlags = window.decorView.systemUiVisibility
                confirmDialog.setOnShowListener {
                    val flags = (
                        android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
                    window.decorView.systemUiVisibility = flags
                    confirmDialog.window?.decorView?.systemUiVisibility = flags
                    confirmDialog.window?.setFlags(
                        android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    )
                    confirmDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    val bottomSheet = confirmDialog.delegate.findViewById<android.view.View>(com.google.android.material.R.id.design_bottom_sheet)
                    bottomSheet?.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                }
                confirmDialog.setOnDismissListener {
                    window.decorView.systemUiVisibility = originalConfirmUiFlags
                }
                val confirmDialogWindow = confirmDialog.window
                if (confirmDialogWindow != null) {
                    confirmDialogWindow.navigationBarColor = android.graphics.Color.TRANSPARENT
                    confirmDialogWindow.setBackgroundDrawableResource(android.R.color.transparent)
                }
                // Exit button logic
                val exitButton = confirmView.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.exit_button)
                exitButton?.setOnClickListener { confirmDialog.dismiss() }
                // Confirm button logic
                val confirmButton = confirmView.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.confirm_button)
                confirmButton?.setOnClickListener { confirmDialog.dismiss() /* Add your complete logic here */ }
                confirmDialog.show()
            }
            // Add Abort Ride button click logic
            val abortRideButton = view.findViewById<android.view.View>(R.id.abort_ride_button)
            abortRideButton?.setOnClickListener {
                val abortDialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
                val abortView = layoutInflater.inflate(R.layout.abort_ride_confirm_bottom_sheet, null)
                abortDialog.window?.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                abortDialog.setContentView(abortView)
                val originalAbortUiFlags = window.decorView.systemUiVisibility
                abortDialog.setOnShowListener {
                    val flags = (
                        android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
                    window.decorView.systemUiVisibility = flags
                    abortDialog.window?.decorView?.systemUiVisibility = flags
                    abortDialog.window?.setFlags(
                        android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    )
                    abortDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    val bottomSheet = abortDialog.delegate.findViewById<android.view.View>(com.google.android.material.R.id.design_bottom_sheet)
                    bottomSheet?.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                }
                abortDialog.setOnDismissListener {
                    window.decorView.systemUiVisibility = originalAbortUiFlags
                }
                val abortDialogWindow = abortDialog.window
                if (abortDialogWindow != null) {
                    abortDialogWindow.navigationBarColor = android.graphics.Color.TRANSPARENT
                    abortDialogWindow.setBackgroundDrawableResource(android.R.color.transparent)
                }
                // Exit button logic
                val exitButton = abortView.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.exit_button)
                exitButton?.setOnClickListener { abortDialog.dismiss() }
                // Abort Ride button logic
                val abortRideConfirmButton = abortView.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.abort_ride_button)
                abortRideConfirmButton?.setOnClickListener { abortDialog.dismiss() /* Add your abort logic here */ }
                abortDialog.show()
            }
            dialog.show()
        }

    // Tab content containers
    val upcomingContainer = findViewById<android.widget.LinearLayout>(R.id.tab_content_upcoming)
    val missedContainer = findViewById<android.widget.LinearLayout>(R.id.tab_content_missed)
    val completedContainer = findViewById<android.widget.LinearLayout>(R.id.tab_content_completed)

        // Set TabLayout tab text size to 14px for all tabs
        val tabLayout = findViewById<com.google.android.material.tabs.TabLayout>(R.id.route_tabs)
        tabLayout.post {
            for (i in 0 until tabLayout.tabCount) {
                val tab = tabLayout.getTabAt(i)
                if (tab != null) {
                    val context = tabLayout.context
                    val tabText = tab.text ?: ""
                    val tabView = android.widget.LinearLayout(context)
                    tabView.orientation = android.widget.LinearLayout.HORIZONTAL
                    tabView.gravity = android.view.Gravity.CENTER

                    val textView = android.widget.TextView(context)
                    textView.text = tabText
                    textView.setTextColor(if (i == tabLayout.selectedTabPosition) 0xFF2F3F46.toInt() else 0xFF4F5567.toInt())
                    textView.textSize = 14f
                    textView.typeface = if (i == tabLayout.selectedTabPosition) android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.BOLD) else android.graphics.Typeface.create("sans-serif-medium", android.graphics.Typeface.NORMAL)
                    tabView.addView(textView)

                    val rectContainer = android.widget.FrameLayout(context)
                    val params = android.widget.LinearLayout.LayoutParams(
                        (25 * context.resources.displayMetrics.density).toInt(),
                        (18 * context.resources.displayMetrics.density).toInt()
                    )
                    params.leftMargin = (5 * context.resources.displayMetrics.density).toInt()
                    params.gravity = android.view.Gravity.CENTER_VERTICAL
                    rectContainer.layoutParams = params
                    // Set rectangle background color based on tab
                    val rectColor = when (i) {
                        0 -> android.graphics.Color.parseColor("#DADCE2") // Upcoming
                        1 -> android.graphics.Color.parseColor("#FFDFDE") // Missed
                        2 -> android.graphics.Color.parseColor("#DDEDD5") // Completed
                        else -> android.graphics.Color.parseColor("#DADCE2")
                    }
                    val rectDrawable = android.graphics.drawable.GradientDrawable()
                    rectDrawable.setColor(rectColor)
                    rectDrawable.cornerRadius = 9 * context.resources.displayMetrics.density
                    rectContainer.background = rectDrawable

                    val numberText = android.widget.TextView(context)
                    numberText.text = "12"
                    numberText.setTextColor(0xFF2F3F46.toInt())
                    numberText.textSize = 13f
                    numberText.typeface = android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.BOLD)
                    numberText.gravity = android.view.Gravity.CENTER
                    numberText.layoutParams = android.widget.FrameLayout.LayoutParams(
                        android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                        android.widget.FrameLayout.LayoutParams.MATCH_PARENT
                    )
                    rectContainer.addView(numberText)
                    tabView.addView(rectContainer)

                    tab.customView = tabView
                }
            }
        }

        // Tab selection logic to show only the selected tab's content
        tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        upcomingContainer.visibility = android.view.View.VISIBLE
                        missedContainer.visibility = android.view.View.GONE
                        completedContainer.visibility = android.view.View.GONE
                    }
                    1 -> {
                        upcomingContainer.visibility = android.view.View.GONE
                        missedContainer.visibility = android.view.View.VISIBLE
                        completedContainer.visibility = android.view.View.GONE
                    }
                    2 -> {
                        upcomingContainer.visibility = android.view.View.GONE
                        missedContainer.visibility = android.view.View.GONE
                        completedContainer.visibility = android.view.View.VISIBLE
                    }
                }
            }
            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })

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
