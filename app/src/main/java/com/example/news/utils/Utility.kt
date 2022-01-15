package com.example.news.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utility {
    /**
     * This function formats the date returned from news api to a more readable format e.g (2021/12/21)
     */
    fun formatDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK)
        val outputFormat = SimpleDateFormat("yyyy/MM/dd", Locale.UK)
        try {
            return outputFormat.format(inputFormat.parse(date) ?: "")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

}