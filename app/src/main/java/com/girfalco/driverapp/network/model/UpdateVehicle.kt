package com.girfalco.driverapp.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateVehicleRequest(
    val vehicleID: Int,
    val driverID: Int
)

@Serializable
data class UpdateVehicleResponse(
    val success: Boolean,
    val message: String,
    val data: Vehicle? // The detailed vehicle object from the response
)
