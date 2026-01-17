package com.example.appbudaya.model

// Generic response dari API
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)
