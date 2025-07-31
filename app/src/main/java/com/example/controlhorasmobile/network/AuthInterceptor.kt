package com.example.controlhorasmobile.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: () -> String?,
    private val acceptHeader: String = "application/json"
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
       // Obtenemos el token dinámicamente
        val token = tokenProvider()?.takeIf { it.isNotBlank() }

        val builder = chain.request().newBuilder().apply {
            token?.let { header("Authorization","Bearer $it") }
            header("Accept", acceptHeader)
        }

        return chain.proceed(builder.build())
    }
}