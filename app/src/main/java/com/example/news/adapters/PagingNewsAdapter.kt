package com.example.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.news.databinding.ItemArticlePreviewBinding
import com.example.news.models.Article

class PagingNewsAdapter : PagingDataAdapter<Article, PagingNewsAdapter.ArticleViewHolder>(
    NewsDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemArticlePreviewBinding.inflate(layoutInflater, parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article =getItem(position)
        if (article != null) {
            holder.bind(article)
        }
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    inner class ArticleViewHolder constructor(private val binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Article) {
            binding.model = item
            binding.mainView.setOnClickListener {
                onItemClickListener?.let { it(item) }
            }
            binding.executePendingBindings()
        }
    }
}