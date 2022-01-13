package com.example.news.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.news.di.ApplicationScope
import com.example.news.models.Article
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao
    abstract fun newsRemoteKeyDao(): AllNewsRemoteKeyDao

    class Callback @Inject constructor(@ApplicationScope private val applicationScope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
        }
    }
}