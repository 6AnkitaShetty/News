package com.example.news.db

import androidx.paging.PagingSource
import androidx.room.*
import com.example.news.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun saveArticle(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): PagingSource<Int, Article>

    @Delete
    suspend fun deleteArticle(article: Article)

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun saveArticles(articles: List<Article>)

}