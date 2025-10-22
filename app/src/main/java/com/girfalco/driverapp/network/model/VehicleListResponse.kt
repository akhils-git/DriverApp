package com.girfalco.driverapp.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Vehicles(
    val success: Boolean,
    val data: List<Vehicle>? = null,
    val totalCount: Int? = null,
    val selfAssignedVehicle: Vehicle? = null
)

@Serializable
data class Vehicle(
    val ID: Int,
    val Name: String,
    val MakeID: Int? = null,
    val ModelID: Int? = null,
    val CompanyID: Int,
    val NumberPlate: String,
    val IMEI: String? = null,
    val DriverID: Int? = null,
    val Make: String? = null,
    val Model: String? = null,
    val activeKey: Boolean? = null,
    val Assignees: String? = null,
    val SelfAssigned: Boolean? = null
)
