package com.girfalco.driverapp.ui.components.home_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.girfalco.driverapp.R
import com.girfalco.driverapp.network.model.Vehicle

class VehicleAdapter(
    private val vehicles: List<Vehicle>,
    private var selectedVehicle: Vehicle?,
    private val onVehicleSelected: (Vehicle) -> Unit
) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    inner class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vehicleLayout: LinearLayout = itemView.findViewById(R.id.vehicle_information_checkbox)
        val vehicleNumber: TextView = itemView.findViewById(R.id.vehicle_number)
        val vehicleCode: TextView = itemView.findViewById(R.id.vehicle_code)
        val vehicleMake: TextView = itemView.findViewById(R.id.vehicle_make)
        val vehicleModel: TextView = itemView.findViewById(R.id.vehicle_model)
        val vehicleCheckbox: ImageView = itemView.findViewById(R.id.vehicle_checkbox)

        fun bind(vehicle: Vehicle) {
            vehicleNumber.text = vehicle.NumberPlate
            vehicleCode.text = vehicle.Name
            vehicleMake.text = "Make: ${vehicle.Make}"
            vehicleModel.text = "Model: ${vehicle.Model}"

            val isSelected = vehicle == selectedVehicle
            updateCheckboxState(isSelected)

            vehicleLayout.setOnClickListener { // Allow tapping the whole card to select
                if (selectedVehicle != vehicle) {
                    val oldSelectedPosition = vehicles.indexOf(selectedVehicle)
                    selectedVehicle = vehicle
                    onVehicleSelected(vehicle)
                    updateCheckboxState(true)
                    if (oldSelectedPosition != -1) {
                        notifyItemChanged(oldSelectedPosition)
                    }
                }
            }
            vehicleCheckbox.setOnClickListener { // Also allow tapping the checkbox to select
                if (selectedVehicle != vehicle) {
                    val oldSelectedPosition = vehicles.indexOf(selectedVehicle)
                    selectedVehicle = vehicle
                    onVehicleSelected(vehicle)
                    updateCheckboxState(true)
                    if (oldSelectedPosition != -1) {
                        notifyItemChanged(oldSelectedPosition)
                    }
                }
            }
        }

        private fun updateCheckboxState(isChecked: Boolean) {
            if (isChecked) {
                vehicleCheckbox.setImageResource(R.drawable.checkbox_checked)
                vehicleLayout.setBackgroundResource(R.drawable.vehicle_information_checkbox_bg_checked)
            } else {
                vehicleCheckbox.setImageResource(R.drawable.checkbox_unchecked)
                vehicleLayout.setBackgroundResource(R.drawable.vehicle_information_checkbox_bg_unchecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vehicle_list_item, parent, false)
        return VehicleViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        holder.bind(vehicles[position])
    }

    override fun getItemCount(): Int = vehicles.size

    fun setSelectedVehicle(vehicle: Vehicle?) {
        val oldSelected = selectedVehicle
        selectedVehicle = vehicle
        if (oldSelected != null) {
            val oldPosition = vehicles.indexOf(oldSelected)
            if (oldPosition != -1) notifyItemChanged(oldPosition)
        }
        if (selectedVehicle != null) {
            val newPosition = vehicles.indexOf(selectedVehicle)
            if (newPosition != -1) notifyItemChanged(newPosition)
        }
    }

    fun getSelectedVehicle(): Vehicle? = selectedVehicle
}
