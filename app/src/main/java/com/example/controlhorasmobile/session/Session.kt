package com.example.controlhorasmobile.session

import android.content.Context

object Session {
    fun obtenerIdUsuarioDesdePrefs(context: Context): Long {
        val prefs = context.getSharedPreferences("usuario", Context.MODE_PRIVATE)
        return prefs.getLong("idUsuario", -1L)
    }

    fun obtenerUsername(context: Context) : String {
        val prefs  = context.getSharedPreferences("usuario", Context.MODE_PRIVATE)
        return prefs.getString("username", "") ?: ""
    }

    fun cerrarSession(context: Context) {
        val prefs  = context.getSharedPreferences("usuario", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}