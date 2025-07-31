package com.example.controlhorasmobile.network

import com.example.controlhorasmobile.model.DatosActivacion
import com.example.controlhorasmobile.model.dto.UsuarioDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthService {
    @POST("api/login")
    suspend fun loginUsuario(@Body request: LoginRequest): Response<UsuarioDTO>

    @POST("api/enviarToken")
    suspend fun enviarToken(
        @Body emailRequest: Map<String,String>
    ): Response<Unit>

    @POST("api/activarToken")
    suspend fun activarCuenta(
        @Body tokenRequest: TokenRequest
    ): Response<DatosActivacion>
}