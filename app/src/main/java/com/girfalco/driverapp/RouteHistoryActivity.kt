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
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.girfalco.driverapp.databinding.ActivityRouteHistoryBinding

class RouteHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRouteHistoryBinding
    private var startDate: String? = null
    private var endDate: String? = null

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
        val calendarView = CalendarView(this)
        calendarView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val cardView = CardView(this)
        cardView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        cardView.radius = 12f
        cardView.cardElevation = 20f
        cardView.setCardBackgroundColor(Color.WHITE)
        cardView.addView(calendarView)

        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val margin = (20 * displayMetrics.density).toInt()
        val popupWidth = screenWidth - 2 * margin

        val popup = PopupWindow(cardView, popupWidth, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        popup.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        calendarView.setOnDateChangeListener { view: CalendarView, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            if (startDate == null) {
                startDate = selectedDate
                binding.dateText.text = startDate
            } else if (endDate == null) {
                endDate = selectedDate
                binding.dateText.text = "$startDate - $endDate"
                popup.dismiss()
                binding.datePickerControl.background = getDrawable(R.drawable.date_picker_background)
                startDate = null
                endDate = null
            }
        }

        popup.setOnDismissListener {
            binding.datePickerControl.background = getDrawable(R.drawable.date_picker_background)
            startDate = null
            endDate = null
        }

        binding.datePickerControl.background = getDrawable(R.drawable.date_picker_background_active)
        popup.showAsDropDown(binding.datePickerControl, margin, 10)
    }
}