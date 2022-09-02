package com.antoniocostadossantos.newsappmvp.presenter.favorite

import com.antoniocostadossantos.newsappmvp.local.model.Article
import com.antoniocostadossantos.newsappmvp.repository.NewsDataSource
import com.antoniocostadossantos.newsappmvp.presenter.ViewHome

class FavoritePresenter(
    val view: ViewHome.Favorite,
    private val DataSource: NewsDataSource
) : FavoriteHome.Presenter {

    fun saveArticle(article: Article) {
        this.DataSource.saveArticle(article)
    }

    override fun onSucess(article: List<Article>) {
        this.view.showArticles(article)
    }

    fun getAll() {
        this.DataSource.getAll(this)
    }

    fun deleteArticle(article: Article){
        this.DataSource.deleteArticle(article)
    }
}