package com.girfalco.driverapp.network

import com.girfalco.driverapp.network.model.RouteListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleApi {
    @GET("v2/scheduleV2/list")
    suspend fun getRouteList(
        @Query("companyID") companyID: Int,
        @Query("vehicleID") vehicleID: Int,
        @Query("routeType") routeType: Int = 3,
        @Query("row") row: Int = 10,
        @Query("page") page: Int = 1,
        @Query("driverApp") driverApp: Int = 1,
        @Query("search") search: String = "",
        @Query("status") status: String = "All",
        @Query("timeZone") timeZone: String
    ): Response<RouteListResponse>
}
