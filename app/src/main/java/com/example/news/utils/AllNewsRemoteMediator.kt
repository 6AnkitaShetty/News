package com.example.news.utils

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.news.api.ApiDataSource
import com.example.news.db.ArticleDatabase
import com.example.news.models.AllNewsRemoteKey
import com.example.news.models.Article
import com.example.news.utils.Constants.NEWS_STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException
import androidx.room.withTransaction

@ExperimentalPagingApi
class AllNewsRemoteMediator(
private val apiDataSource: ApiDataSource,
private val articleDb: ArticleDatabase
) : RemoteMediator<Int, Article>(){

    private val newsArticleDao = articleDb.getArticleDao()
    private val remoteKeysDao = articleDb.newsRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> NEWS_STARTING_PAGE_INDEX
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> remoteKeysDao.getRemoteKeys().nextPageKey
            }
            val response = apiDataSource.getBreakingNews(
                pageSize = state.config.pageSize,
                page = loadKey
            )
            val listing = response.articles
            val articles = listing.map { it }
            val nextPageKey = loadKey + 1
            val prevPageKey = loadKey - 1
            articleDb.withTransaction {
                remoteKeysDao.saveRemoteKeys(
                    AllNewsRemoteKey(
                        0,
                        nextPageKey = nextPageKey,
                        prevPageKey = prevPageKey
                    )
                )
                newsArticleDao.saveArticles(articles)
            }
            MediatorResult.Success(endOfPaginationReached = listing.isEmpty())

        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }
}