package com.example.news.repository

import com.example.news.api.NewApi
import com.example.news.db.ArticleDao
import com.example.news.models.Article
import javax.inject.Inject

class NewsRepository  @Inject constructor(
    private val articleDao: ArticleDao,
    private val newsApi: NewApi
)  {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        newsApi.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        newsApi.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = articleDao.upsert(article)

    fun getSavedNews() = articleDao.getAllArticles()

    suspend fun deleteArticle(article: Article) = articleDao.deleteArticle(article)
}