package com.antoniocostadossantos.newsappmvp.ui.fragments.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antoniocostadossantos.newsappmvp.R
import com.antoniocostadossantos.newsappmvp.databinding.FragmentFavoriteBinding
import com.antoniocostadossantos.newsappmvp.local.db.ArticleDataBase
import com.antoniocostadossantos.newsappmvp.local.remote.RetrofitInstance
import com.antoniocostadossantos.newsappmvp.repository.NewsRepository
import com.antoniocostadossantos.newsappmvp.ui.adapter.MainAdapter
import com.antoniocostadossantos.newsappmvp.ui.fragments.base.BaseFragment
import com.antoniocostadossantos.newsappmvp.util.hide
import com.antoniocostadossantos.newsappmvp.util.show
import com.antoniocostadossantos.newsappmvp.util.state.ArticleListEvent
import com.antoniocostadossantos.newsappmvp.util.state.ArticleListState
import com.antoniocostadossantos.newsappmvp.util.state.StateResource
import com.antoniocostadossantos.newsappmvp.util.toast
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : BaseFragment<FavoriteViewModel, FragmentFavoriteBinding>() {

    private val mainAdapter by lazy {
        MainAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.dispatch(ArticleListEvent.Fetch)
        observeResults()
        setupRecyclerView()
    }

    override fun getViewModel(): Class<FavoriteViewModel> = FavoriteViewModel::class.java

    override fun getFragmentRepository(): NewsRepository =
        NewsRepository(RetrofitInstance.api, ArticleDataBase.invoke(requireContext()))

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteBinding = FragmentFavoriteBinding.inflate(inflater, container, false)

    val itemTouchPerCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val adapterPosition = viewHolder.layoutPosition
            val article = mainAdapter.differ.currentList[adapterPosition]
            viewModel.deleteArticle(article)
            Snackbar.make(
                viewHolder.itemView,
                getString(R.string.article_delete_successful),
                Snackbar.LENGTH_SHORT
            ).apply {
                setAction(getString(R.string.undo)) {
                    viewModel.saveArticle(article)
                    mainAdapter.notifyDataSetChanged()
                }
                show()
            }
            observeResults()
        }
    }

    private fun setupRecyclerView() {
        with(binding.recyclerViewFavorite) {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            ItemTouchHelper(itemTouchPerCallback).attachToRecyclerView(this)
        }

        mainAdapter.setOnClickListener { article ->
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToWebViewFragment(article)
            findNavController().navigate(action)
        }


    }

    private fun observeResults() {
        viewModel.favorite.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ArticleListState.Success -> {
                    binding.recyclerViewFavorite.show()
                    binding.empytList.visibility = View.INVISIBLE
                    mainAdapter.differ.submitList(response.list)
                }
                is ArticleListState.ErrorMessage -> {
                    binding.recyclerViewFavorite.hide()
                    toast("Ocorreu um erro")
                }

                is ArticleListState.Loading -> {
                    binding.recyclerViewFavorite.hide()
                }

                ArticleListState.Empty -> {
                    binding.empytList.show()
                    mainAdapter.differ.submitList(emptyList())
                }
            }
        }
    }
}