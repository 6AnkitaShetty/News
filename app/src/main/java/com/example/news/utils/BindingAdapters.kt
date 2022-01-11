package com.example.news.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.news.models.Article

@BindingAdapter("displayImage")
fun ImageView.setDisplayImage(item: Article) {
    Glide.with(this)
        .load(item.urlToImage)
        .into(this)
}