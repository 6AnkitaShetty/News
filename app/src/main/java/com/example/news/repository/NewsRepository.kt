package com.example.news.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.news.Resource
import com.example.news.api.ApiDataSource
import com.example.news.db.ArticleDatabase
import com.example.news.models.Article
import com.example.news.models.NewsResponse
import com.example.news.utils.AllNewsRemoteMediator
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@ViewModelScoped
class NewsRepository @Inject constructor(
    private val articleDatabase: ArticleDatabase,
    private val apiDataSource: ApiDataSource,
) : BaseDataSource() {
    private val articleDao = articleDatabase.getArticleDao()

    /**
     * Get recent paginated news with offline support
     */
    fun getRecentNews(): Flow<PagingData<Article>> =
        Pager(
            PagingConfig(
                pageSize = 10,
                maxSize = 20,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            remoteMediator = AllNewsRemoteMediator(
                apiDataSource = apiDataSource,
                articleDb = articleDatabase
            ),
            pagingSourceFactory = {
                articleDao.getAllArticles()
            }
        ).flow

    /**
     * Search for news without pagination and offline support
     */
    suspend fun searchNews(searchQuery: String) : Flow<Resource<NewsResponse>> {
        return flow {
            val result = safeApiCall {
                apiDataSource.searchForNews(searchQuery)
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun savedArticle(article: Article) = articleDao.saveArticle(article)

    fun getSavedNews() = articleDao.getArticles()

    suspend fun deleteArticle(article: Article) = articleDao.deleteArticle(article)
}