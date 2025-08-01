package com.example.controlhorasmobile.model

import android.net.Uri

sealed interface UiEstado {
    object Idle: UiEstado
    object Cargando: UiEstado
    data class Exito(val uri: Uri) : UiEstado
    data class Error(val message: String) : UiEstado
}