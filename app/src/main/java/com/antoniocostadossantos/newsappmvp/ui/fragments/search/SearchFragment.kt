package com.antoniocostadossantos.newsappmvp.ui.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.antoniocostadossantos.newsappmvp.databinding.FragmentSearchBinding
import com.antoniocostadossantos.newsappmvp.local.db.ArticleDataBase
import com.antoniocostadossantos.newsappmvp.local.remote.RetrofitInstance
import com.antoniocostadossantos.newsappmvp.repository.NewsRepository
import com.antoniocostadossantos.newsappmvp.ui.adapter.MainAdapter
import com.antoniocostadossantos.newsappmvp.ui.fragments.base.BaseFragment
import com.antoniocostadossantos.newsappmvp.util.UtilQuerryTextListener
import com.antoniocostadossantos.newsappmvp.util.hide
import com.antoniocostadossantos.newsappmvp.util.show
import com.antoniocostadossantos.newsappmvp.util.state.StateResource
import com.antoniocostadossantos.newsappmvp.util.toast

class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>() {

    private val mainAdapter by lazy { MainAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        search()
        observeResults()
    }

    override fun getViewModel(): Class<SearchViewModel> = SearchViewModel::class.java

    override fun getFragmentRepository(): NewsRepository =
        NewsRepository(RetrofitInstance.api, ArticleDataBase.invoke(requireContext()))

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)

    private fun setupRecyclerView() = with(binding) {
        searchRecycler.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }
        mainAdapter.setOnClickListener { article ->
            val action =
                SearchFragmentDirections.actionSearchFragmentToWebViewFragment(article)
            findNavController().navigate(action)
        }
    }

    private fun search() {
        binding.searchField.setOnQueryTextListener(
            UtilQuerryTextListener(
                this.lifecycle
            ) { newText ->
                newText?.let { querry ->
                    if (querry.isNotEmpty()) {
                        viewModel.fetchSearch(querry)
                        binding.searchProgressBar.show()
                    }

                }
            }
        )
    }

    private fun observeResults() {
        viewModel.search.observe(viewLifecycleOwner) { response ->
            when (response) {
                is StateResource.Success -> {
                    binding.searchProgressBar.hide()
                    response.data?.let { data ->
                        mainAdapter.differ.submitList(data.articles.toList())
                    }
                }
                is StateResource.Error -> {
                    binding.searchProgressBar.hide()
                    toast("Ocorreu um erro")
                }

                is StateResource.Loading -> {
                    binding.searchProgressBar.show()
                }

            }
        }
    }
}