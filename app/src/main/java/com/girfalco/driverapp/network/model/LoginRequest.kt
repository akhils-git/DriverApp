package com.girfalco.driverapp.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
    val mobile: String,
    @SerialName("FCMToken") val fcmToken: String
)
