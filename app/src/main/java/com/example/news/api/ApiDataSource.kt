package com.example.news.api

import javax.inject.Inject

class ApiDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getBreakingNews(page: Int, pageSize: Int) =
        apiService.getBreakingNews(pageNumber = page, pageSize = pageSize)

    suspend fun searchForNews(q: String) = apiService.searchForNews(q)
}