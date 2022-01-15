package com.example.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import com.example.news.databinding.FragmentArticleBinding
import com.example.news.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class ArticleFragment : Fragment() {

    private val viewModel: NewsViewModel by viewModels()
    val args: ArticleFragmentArgs by navArgs()
    private lateinit var binding: FragmentArticleBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.article = article
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url!!)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is NewsViewModel.Event.ShowSuccessMessage -> {
                        Snackbar.make(binding.mainView, event.message, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        }
    }

}