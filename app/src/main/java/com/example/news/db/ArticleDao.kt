package com.example.news.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.example.news.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun saveArticle(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): PagingSource<Int, Article>

    @Query("SELECT * FROM articles WHERE isSaved=1")
    fun getArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun saveArticles(articles: List<Article>)

}