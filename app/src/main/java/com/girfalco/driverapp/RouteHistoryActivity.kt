package com.girfalco.driverapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CalendarView
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.girfalco.driverapp.databinding.ActivityRouteHistoryBinding
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

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
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
}