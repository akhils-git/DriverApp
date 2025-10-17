package com.girfalco.driverapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.girfalco.driverapp.databinding.ActivityRouteHistoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class RouteHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRouteHistoryBinding

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

        binding = ActivityRouteHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.routeHistoryBackArrow.setOnClickListener {
            binding.routeHistoryBackArrow.animate().scaleX(0.85f).scaleY(0.85f).setDuration(80).withEndAction {
                binding.routeHistoryBackArrow.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
                finish()
            }.start()
        }

        // Calendar icon click to show calendar popup
        binding.calendarIcon.setOnClickListener {
            showCalendarPopup()
        }

        // Filter icon click to show filter popup
        binding.routeHistoryFilter.setOnClickListener {
            showFilterPopup()
        }
    }

    private fun showCalendarPopup() {
        binding.datePickerControl.background = getDrawable(R.drawable.date_picker_background_active)
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select Date Range")
            .build()

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDateMillis = selection?.first
            val endDateMillis = selection?.second
            if (startDateMillis != null && endDateMillis != null) {
                val startDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(startDateMillis))
                val endDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(endDateMillis))
                binding.dateText.text = "$startDate - $endDate"
            }
            binding.datePickerControl.background = getDrawable(R.drawable.date_picker_background)
        }

        dateRangePicker.addOnNegativeButtonClickListener {
            binding.datePickerControl.background = getDrawable(R.drawable.date_picker_background)
        }

        dateRangePicker.addOnCancelListener {
            binding.datePickerControl.background = getDrawable(R.drawable.date_picker_background)
        }

        dateRangePicker.show(supportFragmentManager, "date_range_picker")
    }

    private fun showFilterPopup() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.route_history_filter_popup, null)
        dialog.setContentView(view)

        // Hide navigation bar when popup is shown (immersive flags)
        val originalUiFlags = window.decorView.systemUiVisibility
        dialog.setOnShowListener {
            val flags = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN
            )
            window.decorView.systemUiVisibility = flags
            dialog.window?.decorView?.systemUiVisibility = flags
            dialog.window?.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            // Explicitly set the bottom sheet background to transparent
            val bottomSheet = dialog.delegate.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
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

        // Handle checkboxes
        val layoutAll = view.findViewById<LinearLayout>(R.id.filter_option_all)
        val checkboxAll = view.findViewById<ImageView>(R.id.popup_action_checkbox_all)
        val textAll = view.findViewById<TextView>(R.id.popup_action_text_all)

        val layoutCompleted = view.findViewById<LinearLayout>(R.id.filter_option_completed)
        val checkboxCompleted = view.findViewById<ImageView>(R.id.popup_action_checkbox_completed)
        val textCompleted = view.findViewById<TextView>(R.id.popup_action_text_completed)

        val layoutAborted = view.findViewById<LinearLayout>(R.id.filter_option_aborted)
        val checkboxAborted = view.findViewById<ImageView>(R.id.popup_action_checkbox_aborted)
        val textAborted = view.findViewById<TextView>(R.id.popup_action_text_aborted)

        val applyButton = view.findViewById<LinearLayout>(R.id.apply_button)

        // Set initial states: All checked, others unchecked
        val isCheckedList = mutableListOf(true, false, false)
        val layouts = listOf(layoutAll, layoutCompleted, layoutAborted)
        val checkboxes = listOf(checkboxAll, checkboxCompleted, checkboxAborted)
        val texts = listOf(textAll, textCompleted, textAborted)

        for (i in layouts.indices) {
            updateCheckbox(layouts[i], checkboxes[i], texts[i], isCheckedList[i])
        }

        // Set up toggle listeners for each row
        for (i in layouts.indices) {
            val layout = layouts[i]
            val checkbox = checkboxes[i]
            val text = texts[i]
            val toggleListener = View.OnClickListener {
                isCheckedList[i] = !isCheckedList[i]
                updateCheckbox(layout, checkbox, text, isCheckedList[i])
            }
            checkbox.setOnClickListener(toggleListener)
            text.setOnClickListener(toggleListener)
            layout.setOnClickListener(toggleListener)
        }

        applyButton.setOnClickListener {
            // Apply filter logic here
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateCheckbox(layout: LinearLayout, checkbox: ImageView, text: TextView, checked: Boolean) {
        if (checked) {
            checkbox.setImageResource(R.drawable.checkbox_checked)
            layout.background = getDrawable(R.drawable.vehicle_information_checkbox_bg_checked)
            text.setTextColor(Color.parseColor("#2F3F46"))
        } else {
            checkbox.setImageResource(R.drawable.checkbox_unchecked)
            layout.background = getDrawable(R.drawable.filter_popup_checkbox_bg)
            text.setTextColor(Color.parseColor("#2F3F46"))
        }
    }
}