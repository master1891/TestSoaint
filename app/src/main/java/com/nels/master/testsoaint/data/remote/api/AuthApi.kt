package com.nels.master.testsoaint.data.remote.api

import com.nels.master.testsoaint.data.remote.dto.LoginRequest
import com.nels.master.testsoaint.data.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
