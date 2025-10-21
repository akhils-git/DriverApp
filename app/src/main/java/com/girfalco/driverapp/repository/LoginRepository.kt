package com.girfalco.driverapp.repository

import com.girfalco.driverapp.network.RetrofitProvider
import com.girfalco.driverapp.network.AuthApi
import com.girfalco.driverapp.network.model.LoginRequest
import com.girfalco.driverapp.network.model.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class LoginRepository(private val api: AuthApi = RetrofitProvider.authApi) {

    suspend fun login(email: String, password: String, mobile: String, fcmToken: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val req = LoginRequest(email = email, password = password, mobile = mobile, fcmToken = fcmToken)
                val resp = api.login(req)
                // If backend returned success=false (validation or auth failure), treat as failure
                if (resp.success) {
                    Result.success(resp)
                } else {
                    val msg = resp.message ?: "Authentication failed"
                    Result.failure(IOException("HTTP 200: $msg"))
                }
            } catch (e: HttpException) {
                // Extract HTTP status and error body if available to provide clearer error messages
                val code = e.code()
                val errorBody = try { e.response()?.errorBody()?.string() } catch (_: Exception) { null }
                val msg = if (!errorBody.isNullOrBlank()) {
                    "HTTP $code: $errorBody"
                } else {
                    "HTTP $code: ${e.message()}"
                }
                Result.failure(IOException(msg, e))
            } catch (e: IOException) {
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
