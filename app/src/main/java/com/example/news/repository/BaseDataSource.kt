package com.example.news.repository

import com.example.news.Resource
import com.example.news.models.ErrorResponse
import com.google.gson.Gson
import retrofit2.Response
import java.lang.Exception

abstract class BaseDataSource {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resource<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return Resource.Success(body)
                }
            } else {
                val message: ErrorResponse =
                    Gson().fromJson(response.errorBody()!!.charStream(), ErrorResponse::class.java)
                return Resource.Error(message.message)
            }
            return Resource.Error("Something went wrong, try again later")
        } catch (e: Exception) {
            return Resource.Error("Something went wrong")
        }
    }
}