package com.example.news.models

data class ErrorResponse(
    val code: String,
    val message: String,
    val status: String
)