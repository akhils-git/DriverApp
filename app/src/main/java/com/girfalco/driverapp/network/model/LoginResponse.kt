package com.girfalco.driverapp.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val success: Boolean = false,
    val message: String? = null,
    val token: String? = null,
    @SerialName("userID") val userID: Int? = null
) {
    // Backwards-compatible string view that existing code uses (e.g. HomeActivity expects userId as String?)
    val userId: String?
        get() = userID?.toString()
}
