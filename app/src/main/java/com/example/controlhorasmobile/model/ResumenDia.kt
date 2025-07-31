package com.example.controlhorasmobile.model

data class ResumenDia(
    val diaMes: String,
    val diaSemana: String,
    val entrada1: String?,
    val salida1: String?,
    val entrada2: String?,
    val salida2: String?,
    val duracion: String?,
    val totalMinutos : Int
)