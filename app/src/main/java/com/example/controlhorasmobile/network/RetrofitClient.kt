package com.example.controlhorasmobile.network

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // Modo para producción en render
    private const val BASE_URL = "https://control-horas-app-1.onrender.com/"
    // Modo para pruebas en local
    //private const val BASE_URL = "http://10.0.2.2:8080/"


    private fun buildClient(
        tokenProvider: (() ->String?)?= null,
        pdfMode: Boolean = false
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor { message ->
            Log.d("Http", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

       val builder =  OkHttpClient.Builder()
           .addInterceptor(logging)
           .followRedirects(false)
           .followSslRedirects(false)
           .connectTimeout(
               if (pdfMode) 60 else 30,
                TimeUnit.SECONDS
        )
        .readTimeout(
            if (pdfMode) 60 else 30,
            TimeUnit.SECONDS
        )
        tokenProvider?.let { provider ->
            builder.addInterceptor(AuthInterceptor(provider))
        }
        return builder.build()
    }

    /**
     * Creamos un servicio Retrofit de tipo T
     *
     * @param serviceClass Clase de servicio (AuthService::class)
     * @param tokenProvider Opcional. Provee el token para el interceptor
     * @param pdfMode Si es true, usa timeout de 60s y no añade converter JSON
     */
    fun <T> getService(
        serviceClass: Class<T>,
        tokenProvider: (() -> String?)? = null,
        pdfMode: Boolean = false
    ): T {
        val client = buildClient(tokenProvider, pdfMode)
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
        if (!pdfMode) {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            retrofitBuilder.addConverterFactory(GsonConverterFactory.create(gson))
        }
        return retrofitBuilder
            .build()
            .create(serviceClass)
    }
}