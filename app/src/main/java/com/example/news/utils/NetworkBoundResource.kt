package com.example.news.utils

import com.example.news.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
inline fun <ResultType, RequestType> NetworkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true },
    crossinline onFetchSuccess: () -> Unit = {},
    crossinline onFetchFailed: (Throwable) -> Unit = {}
) = channelFlow {
    val data = query().first()
    if (shouldFetch(data)) {
        val loading = launch {
            query().collect { send(Resource.Loading(it)) }
        }
        try {
            saveFetchResult(fetch())
            onFetchSuccess()
            loading.cancel()
            query().collect { send(Resource.Success(it)) }
        } catch (t: Throwable) {
            onFetchFailed(t)
            loading.cancel()
            query().collect { send(Resource.Error(t.toString(), it)) }
        }
    } else {
        query().collect { send(Resource.Success(it)) }
    }
}