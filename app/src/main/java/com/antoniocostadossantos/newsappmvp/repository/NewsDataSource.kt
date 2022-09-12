package com.antoniocostadossantos.newsappmvp.repository

import android.content.Context
import com.antoniocostadossantos.newsappmvp.local.model.Article
import com.antoniocostadossantos.newsappmvp.local.db.ArticleDataBase
import com.antoniocostadossantos.newsappmvp.local.remote.NewsAPI
import com.antoniocostadossantos.newsappmvp.local.remote.RetrofitInstance
import com.antoniocostadossantos.newsappmvp.presenter.favorite.FavoriteHome
import com.antoniocostadossantos.newsappmvp.presenter.news.NewsHome
import com.antoniocostadossantos.newsappmvp.presenter.search.SearchHome
import kotlinx.coroutines.*

class NewsDataSource(context: Context) {

    private var db: ArticleDataBase = ArticleDataBase(context)

    private var newsRepository: NewsRepository = NewsRepository(RetrofitInstance.api, db)

    fun getBreakingNews(callback: NewsHome.Presenter) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = RetrofitInstance.api.getBreakingNews("br", 1)
            if (response.isSuccessful) {
                response.body()?.let { newsResponse ->
                    callback.onSucess(newsResponse)
                }
                callback.onComplete()
            } else {
                callback.onError(response.message())
                callback.onComplete()
            }
        }
    }

    fun searchNews(term: String, callback: SearchHome.Presenter) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = RetrofitInstance.api.searchNews(term, 1)
            if (response.isSuccessful) {
                response.body()?.let { newsResponse ->
                    callback.onSucess(newsResponse)
                }
                callback.onComplete()
            } else {
                callback.onError(response.message())
                callback.onComplete()
            }
        }
    }

    fun saveArticle(article: Article) {
        GlobalScope.launch(Dispatchers.Main) {
            newsRepository.updateInsert(article)
        }
    }

    fun getAll(callback: FavoriteHome.Presenter) {
        var allArticles: List<Article>
        CoroutineScope(Dispatchers.IO).launch {
            allArticles = newsRepository.getAll().value!!

            withContext(Dispatchers.Main) {
                callback.onSucess(allArticles)
            }
        }
    }

    fun deleteArticle(article: Article?) {
        GlobalScope.launch(Dispatchers.Main) {
            article?.let { articleDeleted ->
                newsRepository.delete(articleDeleted)
            }
        }
    }


}