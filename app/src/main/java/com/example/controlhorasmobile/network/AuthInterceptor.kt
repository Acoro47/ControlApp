package com.example.controlhorasmobile.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: () -> String?,
    private val acceptHeader: String = "application/json"
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
       // Obtenemos el token dinámicamente
        val token = tokenProvider()?.takeIf { it.isNotBlank() }
        Log.e("Http","Token recuperado: $token")

        val request = chain.request().newBuilder().apply {
            token?.let {
                addHeader("Authorization","Bearer $it")
                Log.d("Http", "Authorization: Bearer $it")
            }
            addHeader("Accept", acceptHeader)
        }.build()

        return chain.proceed(request)
    }
}