package com.girfalco.driverapp.ui.components.home_screen

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.girfalco.driverapp.R

class VehicleInformationCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    var onChangeButtonClick: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.vehicle_information_card, this, true)

        val changeButton = findViewById<android.widget.TextView>(R.id.change_button)
        changeButton?.setOnClickListener { v ->
            v.animate().scaleX(0.93f).scaleY(0.93f).setDuration(80).withEndAction {
                v.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
            }.start()
            onChangeButtonClick?.invoke()
        }
    }

    fun setVehicleDetails(plateNumber: String?, code: String?, make: String?, model: String?) {
        findViewById<TextView>(R.id.vehicle_number).text = plateNumber
        findViewById<TextView>(R.id.vehicle_code).text = code
        findViewById<TextView>(R.id.vehicle_make).text = "Make: $make"
        findViewById<TextView>(R.id.vehicle_model).text = "Model: $model"
    }
}
