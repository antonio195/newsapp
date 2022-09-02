package com.antoniocostadossantos.newsappmvp.presenter.search

import com.antoniocostadossantos.newsappmvp.local.model.NewsResponse
import com.antoniocostadossantos.newsappmvp.repository.NewsDataSource
import com.antoniocostadossantos.newsappmvp.presenter.ViewHome

class SearchPresenter(
    val view: ViewHome.View,
    private val dataSource: NewsDataSource
) : SearchHome.Presenter {
    override fun search(term: String) {
        this.view.showProgressBar()
        this.dataSource.searchNews(term, this)
    }

    override fun onSucess(newsResponse: NewsResponse) {
        this.view.showArticles(newsResponse.articles)
    }

    override fun onError(message: String) {
        this.view.showFailure(message)
    }

    override fun onComplete() {
        this.view.hideProgressBar()
    }
}