package com.example.appbudaya.model

// Data class untuk user
data class User(
    val id: Int,
    val nama: String,
    val email: String,
    val role: String = "user", // "admin" atau "user"
    val token: String = "" // JWT token untuk authentication
)
