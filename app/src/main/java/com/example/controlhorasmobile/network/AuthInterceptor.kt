package com.example.controlhorasmobile.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: () -> String?
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        // Obtenemos el token dinámicamente
        val token = tokenProvider().orEmpty()
        // Construimos la petición:
        // 1) Authorization con Bearer token
        // 2) Accept: application/pdf para solicitar el pdf
        val request = original.newBuilder()
            .header("Authorization", "Bearer $token")
            .header("Accept", "application/pdf")
            .build()
        return chain.proceed(request)
    }
}