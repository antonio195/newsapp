package com.antoniocostadossantos.newsappmvp.repository

import androidx.lifecycle.LiveData
import com.antoniocostadossantos.newsappmvp.local.model.Article
import com.antoniocostadossantos.newsappmvp.local.db.ArticleDataBase
import com.antoniocostadossantos.newsappmvp.local.remote.NewsAPI

class NewsRepository(
    private val api: NewsAPI,
    private val db: ArticleDataBase
) {

    //    Remote
    suspend fun getAllRemote() = api.getBreakingNews("br", 1)
    suspend fun search(query: String) = api.searchNews(query, 1)


    //    Local
    suspend fun updateInsert(article: Article) = db.getArticlesDao().updateInsert(article)
    fun getAll(): LiveData<List<Article>> = db.getArticlesDao().getAll()
    suspend fun delete(article: Article) = db.getArticlesDao().delete(article)

}