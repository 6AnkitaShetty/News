package com.example.news.models
import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("articles")
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)