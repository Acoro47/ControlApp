package com.example.controlhorasmobile.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.controlhorasmobile.PREFS_NAME
import com.example.controlhorasmobile.TOKEN_KEY
import com.example.controlhorasmobile.network.AuthInterceptor
import com.example.controlhorasmobile.network.InformePdfService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://control-horas-app-1.onrender.com/"


    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /* ------- INTERCEPTORES --------*/
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Named("userAuth")
    fun provideUserAuthInterceptor(
        prefs: SharedPreferences
    ): AuthInterceptor =
        AuthInterceptor(
            tokenProvider = { prefs.getString("TOKEN_KEY",null)},
            acceptHeader = "application/json"
        )

    @Provides
    @Named("pdfAuth")
    fun providePdfAuthInterceptor(
        prefs: SharedPreferences
    ): AuthInterceptor {
        val tokenProvider = { prefs.getString("TOKEN_KEY", null) }
        Log.e("Http", "Token guardado/obtenido = ${prefs.getString(TOKEN_KEY, null)}")
        return AuthInterceptor(
            tokenProvider = { prefs.getString("TOKEN_KEY",null)},
            acceptHeader = "application/pdf"
        )
    }

    /* ------- HTTP CLIENTS --------*/

    @Provides
    @Named("jsonClient")
    fun provideJsonClient(
        logger: HttpLoggingInterceptor,
        @Named("userAuth") auth: AuthInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(auth)
            .build()

    @Provides
    @Named("pdfClient")
    fun providePdfClient(
        logger: HttpLoggingInterceptor,
        @Named("pdfAuth") auth: AuthInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(auth)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

    /* ------- RETROFITS --------*/

    @Provides
    @Named("jsonRetrofit")
    fun provideJsonRetrofit(
        @Named("jsonClient") client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .build()

    @Provides
    @Named("pdfRetrofit")
    fun providePdfRetrofit(
        @Named("pdfClient") client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .build()

    /* ------- SERVICIOS --------*/

    @Provides
    fun provideInformePdfService(
        @Named("pdfRetrofit") retrofit: Retrofit
    ): InformePdfService =
        retrofit.create(InformePdfService::class.java)
}