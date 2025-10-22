package com.girfalco.driverapp.network

/**
 * Simple in-memory store for the current auth token.
 * The token can be updated after login and is read by Retrofit's interceptor at request time.
 * For persistence across launches, save the token to SharedPreferences and restore on app start.
 */
object AuthTokenStore {
    @Volatile
    var token: String? = null
}

