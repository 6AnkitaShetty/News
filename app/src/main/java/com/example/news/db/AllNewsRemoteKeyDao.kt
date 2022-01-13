package com.example.news.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.news.models.AllNewsRemoteKey

@Dao
interface AllNewsRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRemoteKeys(remoteKey: AllNewsRemoteKey)

    @Query("SELECT * FROM all_news_remote_keys ORDER BY id DESC")
    suspend fun getRemoteKeys(): AllNewsRemoteKey

    @Query("DELETE FROM all_news_remote_keys")
    suspend fun clearRemoteKeys()
}