package com.example.news.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Model for pagination using remote mediator
 */
@Entity(tableName = "all_news_remote_keys")
data class AllNewsRemoteKey(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nextPageKey: Int,
    val prevPageKey: Int,
)