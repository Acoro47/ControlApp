package com.example.controlhorasmobile.model.dto

data class LoginRequest(
    val username: String,
    val password: String,
)

data class TokenRequest(
    val token: String
)

data class EmailRequest(
    val email: String
)