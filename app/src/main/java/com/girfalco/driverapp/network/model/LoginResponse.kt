package com.girfalco.driverapp.network.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val success: Boolean = false,
    val message: String? = null,
    val token: String? = null,
    val userId: String? = null
)
