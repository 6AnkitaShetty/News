package com.example.news.api

import com.example.news.models.NewsResponse
import com.example.news.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("language")
        language: String = "en",
        @Query("page")
        pageNumber: Int = 1,
        @Query("pageSize")
        pageSize: Int,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): NewsResponse

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>


}