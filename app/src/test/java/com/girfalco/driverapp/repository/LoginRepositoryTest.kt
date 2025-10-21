package com.girfalco.driverapp.repository

import com.girfalco.driverapp.network.AuthApi
import com.girfalco.driverapp.network.model.LoginRequest
import com.girfalco.driverapp.network.model.LoginResponse
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginRepositoryTest {
    private lateinit var server: MockWebServer
    private lateinit var api: AuthApi

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()

        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun `login success returns response`() = runBlocking {
        val body = "{ \"success\": true, \"message\": \"ok\", \"token\": \"abc\", \"userId\": \"123\" }"
        server.enqueue(MockResponse().setBody(body).setResponseCode(200))

    val repo = LoginRepository(api)
    val result = repo.login("a@b.com", "pw", "1", "token")
    Assert.assertTrue(result.isSuccess)
    val resp = result.getOrNull()
    Assert.assertNotNull(resp)
    Assert.assertEquals("abc", resp?.token)
    }
}
