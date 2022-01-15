package com.example.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.adapters.PagingNewsAdapter
import com.example.news.databinding.FragmentBreakingNewsBinding
import com.example.news.ui.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
@ExperimentalPagingApi
class BreakingNewsFragment : Fragment() {

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var pagingNewsAdapter: PagingNewsAdapter
    lateinit var binding: FragmentBreakingNewsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBreakingNewsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        pagingNewsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_newsDetailFragment,
                bundle
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getBreakingNews().collectLatest { pagingData->
                    pagingNewsAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        pagingNewsAdapter = PagingNewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = pagingNewsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}