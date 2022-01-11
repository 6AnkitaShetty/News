package com.example.news.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.news.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}