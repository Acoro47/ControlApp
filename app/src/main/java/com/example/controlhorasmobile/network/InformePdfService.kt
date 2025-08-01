package com.example.controlhorasmobile.network

import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming

interface InformePdfService {
    @GET("api/informe-mensual/exportarPdf")
    @Streaming
    suspend fun descargarPdf(
        @Query("mes") mes: String? = null
        ): Response<ResponseBody>
}