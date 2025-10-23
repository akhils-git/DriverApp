package com.girfalco.driverapp.network

import com.girfalco.driverapp.network.model.UpdateVehicleRequest
import com.girfalco.driverapp.network.model.UpdateVehicleResponse
import com.girfalco.driverapp.network.model.Vehicles // Changed import from VehicleListResponse to Vehicles
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface VehicleApi {
    @GET("v2/vehicle/listCompanyVehicles")
    suspend fun listCompanyVehicles(
        @Query("cid") companyId: Int,
        @Query("search") search: String? = null,
        @Query("page") page: Int? = null,
        @Query("row") row: Int? = null
    ): Response<Vehicles> // Changed return type to Vehicles

    @POST("v2/scheduleV2/updateSelectedVehicle")
    suspend fun updateSelectedVehicle(
        @Body request: UpdateVehicleRequest
    ): Response<UpdateVehicleResponse>
}
