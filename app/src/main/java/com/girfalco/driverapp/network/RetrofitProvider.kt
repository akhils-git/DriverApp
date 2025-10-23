package com.girfalco.driverapp.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object RetrofitProvider {
    private const val BASE_URL = "https://api.girfalco.sa/"

    private val contentType = "application/json".toMediaType()

    private val json = Json { 
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val req = it.request()
            val newReq = req.newBuilder()
                .header("Authorization", "Bearer ${AuthTokenStore.token}")
                .build()
            it.proceed(newReq)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val personApi: PersonApi = retrofit.create(PersonApi::class.java)
    val vehicleApi: VehicleApi = retrofit.create(VehicleApi::class.java)
    val scheduleApi: ScheduleApi = retrofit.create(ScheduleApi::class.java)
}
