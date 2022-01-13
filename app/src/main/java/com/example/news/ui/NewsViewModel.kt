package com.example.news.ui

import android.net.NetworkCapabilities.*
import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.news.R
import com.example.news.repository.NewsRepository
import com.example.news.Resource
import com.example.news.models.Article
import com.example.news.models.NewsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    private val _searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNews: LiveData<Resource<NewsResponse>> = _searchNews

    private val eventChannel = Channel<Event>()
    val events = eventChannel.receiveAsFlow()

    fun getBreakingNews() : Flow<PagingData<Article>> {
        return newsRepository.getRecentNews().cachedIn(viewModelScope)
    }

    fun searchNews(searchQuery: String) {
        viewModelScope.launch {
            newsRepository.searchNews(searchQuery)
                .catch { e ->
                    _searchNews.value = Resource.Error(e.toString())
                }.collect {
                    _searchNews.value = it
                }
        }
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.save(article)
        eventChannel.send(Event.ShowSuccessMessage(R.string.article_saved_confirmation_message))
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }


    sealed class Event {
        data class ShowSuccessMessage(val message: Int) : Event()
    }

}