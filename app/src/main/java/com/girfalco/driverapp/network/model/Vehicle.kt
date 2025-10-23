package com.girfalco.driverapp.network.model

import kotlinx.serialization.Serializable

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
    val SelfAssigned: Boolean? = null,
    val PlateNumber: String? = NumberPlate // Keep for compatibility if used elsewhere
)
