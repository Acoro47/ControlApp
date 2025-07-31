package com.example.controlhorasmobile.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Streaming

interface InformePdfService {
    @GET("api/informe-mensual/pdf")
    @Streaming
    suspend fun descargarPdf(
        @Header("Authorization") token: String,
        @Query("mes") mes: String
        ): Response<ResponseBody>
}


