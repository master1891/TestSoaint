package com.nels.master.testsoaint.data.remote.api

import com.nels.master.testsoaint.data.remote.dto.CrearRegistroRequest
import com.nels.master.testsoaint.data.remote.dto.RegistroDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RegistroApi {

    @GET("api/registros")
    suspend fun obtenerRegistros(): Response<List<RegistroDto>>

    @POST("api/registros")
    suspend fun crearRegistro(@Body request: CrearRegistroRequest): Response<RegistroDto>
}
