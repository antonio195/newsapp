package com.antoniocostadossantos.newsappmvp.ui.fragments.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.antoniocostadossantos.newsappmvp.repository.NewsRepository
import com.antoniocostadossantos.newsappmvp.ui.fragments.favorite.FavoriteViewModel
import com.antoniocostadossantos.newsappmvp.ui.fragments.home.HomeViewModel
import com.antoniocostadossantos.newsappmvp.ui.fragments.search.SearchViewModel
import com.antoniocostadossantos.newsappmvp.ui.fragments.webView.WebViewViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(
    private val repository: NewsRepository,
    private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository, application) as T
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(repository) as T
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> FavoriteViewModel(repository) as T
            modelClass.isAssignableFrom(WebViewViewModel::class.java) -> WebViewViewModel(repository) as T
            else -> throw IllegalArgumentException("ViewModel não encontada.")
        }
    }
}