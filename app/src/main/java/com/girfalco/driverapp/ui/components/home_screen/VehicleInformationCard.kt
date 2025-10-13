package com.girfalco.driverapp.ui.components.home_screen

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.girfalco.driverapp.R

class VehicleInformationCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.vehicle_information_card, this, true)

        // Add tap animation to the Change button
        val changeButton = findViewById<android.widget.TextView>(R.id.change_button)
        changeButton?.setOnClickListener { v ->
            v.animate().scaleX(0.93f).scaleY(0.93f).setDuration(80).withEndAction {
                v.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
            }.start()
            // TODO: Add your action here if needed
        }
    }
}
