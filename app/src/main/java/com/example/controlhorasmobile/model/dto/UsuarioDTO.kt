package com.example.controlhorasmobile.model.dto

data class UsuarioDTO(
    val id: Long,
    val username: String,
    val mensaje: String,
    val token: String
)

data class TokenResponse(
    val token: String
)