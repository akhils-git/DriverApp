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

    // Cache the TextViews for performance and safety
    private val vehicleNumberTextView: TextView
    private val vehicleCodeTextView: TextView
    private val vehicleMakeTextView: TextView
    private val vehicleModelTextView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.vehicle_information_card, this, true)

        // Initialize the cached views with the correct IDs from the XML
        vehicleNumberTextView = findViewById(R.id.vehicle_number_text)
        vehicleCodeTextView = findViewById(R.id.plate_number_text)
        vehicleMakeTextView = findViewById(R.id.make_text)
        vehicleModelTextView = findViewById(R.id.model_text)

        val changeButton = findViewById<TextView>(R.id.change_button)
        changeButton?.setOnClickListener { v ->
            v.animate().scaleX(0.93f).scaleY(0.93f).setDuration(80).withEndAction {
                v.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
            }.start()
            onChangeButtonClick?.invoke()
        }
    }

    /**
     * Updates the vehicle details displayed on the card.
     * This method now uses the cached TextViews with the correct data mapping.
     */
    fun setVehicleDetails(plateNumber: String?, code: String?, make: String?, model: String?) {
        // The 'code' from the API (e.g., 'RIY - 321') goes into the main vehicle number text view.
        vehicleNumberTextView.text = code ?: "N/A"
        // The 'plateNumber' from the API (e.g., '1837 RXB') goes into the secondary plate number text view.
        vehicleCodeTextView.text = plateNumber ?: "N/A"
        vehicleMakeTextView.text = if (!make.isNullOrBlank()) "Make: $make" else ""
        vehicleModelTextView.text = if (!model.isNullOrBlank()) "Model: $model" else ""
    }
}
