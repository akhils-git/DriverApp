
package com.girfalco.driverapp

import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Button
import android.content.Intent
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
        filterIcon?.setOnClickListener { v ->
            v.animate()
                .scaleX(0.93f)
                .scaleY(0.93f)
                .setDuration(80)
                .withEndAction {
                    v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(80)
                        .withEndAction {
                            showFilterPopup()
                        }
                        .start()
                }
                .start()
        }

        // Set up Change button to show SelectVehicle popup
        val vehicleCard = findViewById<com.girfalco.driverapp.ui.components.home_screen.VehicleInformationCard>(R.id.vehicle_card)
        vehicleCard?.onChangeButtonClick = {
            showSelectVehiclePopup()
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
                        .withEndAction {
                            // Navigate to Route Details screen
                            val intent = Intent(this, RouteDetailsActivity::class.java)
                            startActivity(intent)
                        }
                        .start()
                }
                .start()
        }
    }

    fun showSelectVehiclePopup() {

        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.select_vehicle_bottom_sheet, null)
        dialog.setContentView(view)

        // Force popup height to 640dp
        view.post {
            val bottomSheet = dialog.delegate.findViewById<android.view.View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val params = it.layoutParams
                val density = resources.displayMetrics.density
                params.height = (640 * density).toInt()
                it.layoutParams = params
            }
        }

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

        // Checkbox toggle logic for vehicle_information_checkbox
        val vehicleCheckbox = view.findViewById<ImageView>(R.id.vehicle_checkbox)
        var isChecked = false
        fun updateVehicleCheckbox() {
            if (isChecked) {
                vehicleCheckbox.setImageResource(R.drawable.checkbox_checked)
                // Set checked background color
                view.findViewById<View>(R.id.vehicle_information_checkbox)?.setBackgroundResource(R.drawable.vehicle_information_checkbox_bg_checked)
            } else {
                vehicleCheckbox.setImageResource(R.drawable.checkbox_unchecked)
                // Set unchecked background color
                view.findViewById<View>(R.id.vehicle_information_checkbox)?.setBackgroundResource(R.drawable.vehicle_information_checkbox_bg_unchecked)
            }
        }
        updateVehicleCheckbox()
        vehicleCheckbox.setOnClickListener {
            isChecked = !isChecked
            updateVehicleCheckbox()
        }
        // Also allow tapping the card to toggle the checkbox
        view.findViewById<View>(R.id.vehicle_information_checkbox)?.setOnClickListener {
            isChecked = !isChecked
            updateVehicleCheckbox()
        }

        dialog.show()
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

        // Set text for each filter checkbox
        val allRow = view.findViewById<View>(R.id.filter_option_all)
        val activeRow = view.findViewById<View>(R.id.filter_option_active)
        val notStartedRow = view.findViewById<View>(R.id.filter_option_not_started)
        val delayedRow = view.findViewById<View>(R.id.filter_option_delayed)

        allRow?.findViewById<android.widget.TextView>(R.id.popup_action_text)?.text = "All"
        activeRow?.findViewById<android.widget.TextView>(R.id.popup_action_text)?.text = "Active Routes"
        notStartedRow?.findViewById<android.widget.TextView>(R.id.popup_action_text)?.text = "Not Started"
        delayedRow?.findViewById<android.widget.TextView>(R.id.popup_action_text)?.text = "Delayed Routes"

        // --- Make all checkboxes interactive ---
        val rows = listOf(allRow, activeRow, notStartedRow, delayedRow)
        val isCheckedList = mutableListOf(true, false, false, false)

        fun updateCheckbox(row: View?, checked: Boolean) {
            val checkbox = row?.findViewById<ImageView>(R.id.popup_action_checkbox)
            if (checkbox != null) {
                if (checked) {
                    checkbox.setImageResource(R.drawable.checkbox_checked)
                } else {
                    checkbox.setImageResource(R.drawable.checkbox_unchecked)
                }
            }
            row?.isSelected = checked
        }

        // Initialize all checkboxes
        for (i in rows.indices) {
            updateCheckbox(rows[i], isCheckedList[i])
        }

        // Set up toggle listeners for each row
        for (i in rows.indices) {
            val row = rows[i]
            val checkbox = row?.findViewById<ImageView>(R.id.popup_action_checkbox)
            val text = row?.findViewById<View>(R.id.popup_action_text)
            val toggleListener = View.OnClickListener {
                isCheckedList[i] = !isCheckedList[i]
                updateCheckbox(row, isCheckedList[i])
            }
            checkbox?.setOnClickListener(toggleListener)
            text?.setOnClickListener(toggleListener)
            row?.setOnClickListener(toggleListener)
        }

        // Set Apply button (LinearLayout) to dismiss the dialog
        view.findViewById<View>(R.id.apply_button)?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}