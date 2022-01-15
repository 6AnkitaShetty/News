package com.example.news.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.databinding.FragmentBreakingNewsBinding
import com.example.news.databinding.FragmentNewsDetailBinding
import com.example.news.utils.Utility

class NewsDetailFragment : Fragment() {

    lateinit var binding: FragmentNewsDetailBinding
    private val args: NewsDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsDetailBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        handleClicks()
    }

    private fun setUpViews() {

        //load image using glide
        Glide.with(binding.imgNews)
            .load(args.article.urlToImage)
            .into(binding.imgNews)

        //display date
        binding.txtDate.text = args.article.publishedAt?.let { Utility.formatDate(it) }

        //display title
        binding.txtTitle.text = args.article.title

        //display source
        binding.txtSource.text = args.article.source?.name

        //display article
        binding.txtArticle.text = args.article.content

    }

    private fun handleClicks() {
        //on click view full article, open webView
        binding.btnViewFullArticle.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", args.article)
            }
            findNavController().navigate(
                R.id.action_newsDetailFragment_to_articleFragment,
                bundle
            )
        }
    }
}