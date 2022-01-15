package com.example.news.models
import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("articles")
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)