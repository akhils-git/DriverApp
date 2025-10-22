package com.girfalco.driverapp.network

import com.girfalco.driverapp.network.model.PersonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PersonApi {
    @GET("person/getPersonById")
    suspend fun getPersonById(@Query("id") id: Int): Response<PersonResponse>
}
