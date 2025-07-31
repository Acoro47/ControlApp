package com.example.controlhorasmobile.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // Modo para producción en render
    const val BASE_URL = "https://control-horas-app-1.onrender.com/"
    // Modo para pruebas en local
    //private const val BASE_URL = "http://10.0.2.2:8080/"


    fun buildClient(tokenProvider: (() ->String?)?= null): OkHttpClient {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }

       return OkHttpClient.Builder()
           .addInterceptor(logging)
           .also { builder ->
               tokenProvider?.let { provider ->
                   builder.addInterceptor(AuthInterceptor(provider))
               }
           }
           .connectTimeout(30, TimeUnit.SECONDS)
           .readTimeout(30, TimeUnit.SECONDS)
           .build()
    }

    fun buildPdfClient(tokenProvider: (() -> String?)? = null): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            })
            .also { builder ->
                tokenProvider?.let { builder.addInterceptor(AuthInterceptor(it)) }
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

    }

    fun getPdfRetrofit(tokenProvider: (() -> String?)? = null): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(buildPdfClient(tokenProvider))
            .build()


    private fun getJsonRetrofit(tokenProvider: (() -> String?)? = null): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(buildClient(tokenProvider))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun getAuthService(): AuthService {
        return getJsonRetrofit().create(AuthService::class.java)
    }

    // Servicio de usuario e informes reciben tokenProvider
    fun getUsuarioService(tokenProvider: () -> String?): UsuarioService =
        getJsonRetrofit(tokenProvider).create(UsuarioService::class.java)

    fun getInformeService(tokenProvider: () -> String?): InformePdfService =
        getPdfRetrofit(tokenProvider).create(InformePdfService::class.java)
}