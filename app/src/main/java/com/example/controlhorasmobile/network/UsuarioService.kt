package com.example.controlhorasmobile.network

import com.example.controlhorasmobile.model.DatosActivacion
import com.example.controlhorasmobile.model.dto.RegistroDTO
import com.example.controlhorasmobile.model.dto.UsuarioDTO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UsuarioService {

    data class MensajeRespuesta(
        val estado: String,
        val mensaje: String
    )

    data class LoginRequest(
        val username: String,
        val password: String
    )

    @GET("activar")
    suspend fun activarCuenta(@Query("token") token:String): Response<DatosActivacion>

    @GET("api/enviarToken")
    suspend fun solicitarToken(
        @Query("username") username:String,
        @Query("email") email:String
    ): Response<MensajeRespuesta>


    @POST("api/login")
    suspend fun loginUsuario(
        @retrofit2.http.Body loginRequest: LoginRequest
    ): Response<UsuarioDTO>

    @GET("api/registros")
    suspend fun obtenerRegistros(
        @Query("id") idUsuario: Long,
        @Query("fechaDesde") fechaDesde: String,
        @Query("fechaHasta") fechaHasta: String
    ): List<RegistroDTO>

    @FormUrlEncoded
    @POST("api/entrada")
    suspend fun registrarEntrada(@Field("idUsuario") idUsuario: Long): Response<Any>

    @FormUrlEncoded
    @POST("api/salida")
    suspend fun registrarSalida(@Field("idUsuario") idUsuario: Long): Response<Any>

}

