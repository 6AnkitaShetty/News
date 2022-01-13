package com.example.news.di

import android.content.Context
import androidx.room.Room
import com.example.news.api.ApiDataSource
import com.example.news.api.HttpInterceptor
import com.example.news.api.ApiService
import com.example.news.db.ArticleDatabase
import com.example.news.utils.Constants
import com.example.news.utils.Constants.DATABASE_NAME
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideArticleDatabase(
        @ApplicationContext context: Context,callback:ArticleDatabase.Callback
    ) = Room.databaseBuilder(context, ArticleDatabase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Singleton
    @Provides
    fun provideArticleDao(
        database: ArticleDatabase
    ) = database.getArticleDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun providesApplicationScope() = CoroutineScope(SupervisorJob())

    @Provides
    fun providesGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(
            OkHttpClient.Builder().also { client ->
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                client.addInterceptor(logging)
                client.addInterceptor(HttpInterceptor())
                client.connectTimeout(120, TimeUnit.SECONDS)
                client.readTimeout(120, TimeUnit.SECONDS)
                client.protocols(Collections.singletonList(Protocol.HTTP_1_1))
            }.build()
        )
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiClient(apiService: ApiService) : ApiDataSource {
        return ApiDataSource(apiService)
    }

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope