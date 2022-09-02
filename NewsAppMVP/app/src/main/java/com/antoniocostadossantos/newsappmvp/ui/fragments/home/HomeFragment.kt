package com.antoniocostadossantos.newsappmvp.ui.fragments.home

import android.app.ProgressDialog.show
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.antoniocostadossantos.newsappmvp.databinding.FragmentHomeBinding
import com.antoniocostadossantos.newsappmvp.local.db.ArticleDataBase
import com.antoniocostadossantos.newsappmvp.local.remote.RetrofitInstance
import com.antoniocostadossantos.newsappmvp.repository.NewsRepository
import com.antoniocostadossantos.newsappmvp.ui.adapter.MainAdapter
import com.antoniocostadossantos.newsappmvp.ui.fragments.base.BaseFragment
import com.antoniocostadossantos.newsappmvp.util.hide
import com.antoniocostadossantos.newsappmvp.util.show
import com.antoniocostadossantos.newsappmvp.util.state.StateResource
import com.antoniocostadossantos.newsappmvp.util.toast

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    private val mainAdapter by lazy { MainAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeResults()
    }

    private fun observeResults() {
        viewModel.getAll.observe(viewLifecycleOwner) { response ->
            when (response) {
                is StateResource.Success -> {
                    binding.progressBar.hide()
                    response.data?.let { data ->
                        mainAdapter.differ.submitList(data.articles.toList())
                    }
                }
                is StateResource.Error -> {
                    binding.progressBar.hide()
                    toast("Ocorreu um erro")
                }

                is StateResource.Loading -> {
                    binding.progressBar.show()
                }

            }
        }
    }

    private fun setupRecyclerView() = with(binding) {
        recyclerView.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }
        mainAdapter.setOnClickListener { article ->
            val action =
                HomeFragmentDirections.actionHomeFragmentToWebViewFragment(article)
            findNavController().navigate(action)
        }
    }

    override fun getViewModel(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun getFragmentRepository(): NewsRepository =
        NewsRepository(RetrofitInstance.api, ArticleDataBase.invoke(requireContext()))

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
}