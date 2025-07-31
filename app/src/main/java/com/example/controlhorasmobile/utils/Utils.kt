package com.example.controlhorasmobile.utils

import android.content.SharedPreferences
import androidx.navigation.NavController

fun String.capitalizar(): String = replaceFirstChar { it.uppercaseChar() }


fun cerrarSesion(navController: NavController, prefs: SharedPreferences){
    prefs.edit().clear().apply()
    navController.navigate("login"){
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

fun formatearMinutos(minutos: Int): String {
    val h = minutos / 60
    val m = minutos % 60
    return "${h} h ${m} m"
}