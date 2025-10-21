package com.girfalco.driverapp.network

import com.girfalco.driverapp.network.model.LoginRequest
import com.girfalco.driverapp.network.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("v2/user/Applogin")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
