package com.girfalco.driverapp.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Vehicles(
    val success: Boolean,
    val data: List<Vehicle>? = null,
    val totalCount: Int? = null,
    val selfAssignedVehicle: Vehicle? = null
)
