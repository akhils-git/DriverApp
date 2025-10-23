package com.girfalco.driverapp.network.model

import kotlinx.serialization.Serializable

@Serializable
data class RouteListResponse(
    val success: Boolean,
    val data: List<RouteInformation>?,
    val count: Int? = null,
    val activeKey: Boolean? = null
)

@Serializable
data class RouteInformation(
    val RouteID: Int,
    val RouteName: String,
    val Date: String? = null,
    val timeZoneDate: String? = null,
    val TerritoryName: String? = null,
    val RouteType: String? = null,
    val Coordinates: List<Coordinate>? = null,
    val PolylinePath: List<PolylinePoint>? = null,
    val Stops: Int? = null,
    val Distance: String? = null,
    val StartTime: String? = null,
    val EndTime: String? = null,
    val Repeat: String? = null,
    val ScheduleDate: String? = null,
    val VehicleID: String? = null,
    val VehicleName: String? = null,
    val DriverName: String? = null,
    val totalBins: Int? = null,
    val collectedBins: Int? = null,
    val pendingBins: Int? = null,
    val status: String? = null,
    val statusLabel: String? = null
)

@Serializable
data class Coordinate(
    val index: Int? = null,
    val action: Int? = null,
    val comment: String? = null,
    val lat: String? = null,
    val lng: String? = null,
    val location: String? = null,
    val binDetails: BinDetails? = null,
    val completedAt: String? = null,
    val note: String? = null
)

@Serializable
data class BinDetails(
    val binId: Int? = null,
    val binLabel: String? = null,
    val binType: String? = null,
    val percentageFilled: Int? = null
)

@Serializable
data class PolylinePoint(
    val lat: Double? = null,
    val lng: Double? = null
)
