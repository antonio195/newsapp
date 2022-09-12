package com.antoniocostadossantos.newsappmvp.util.state

import com.antoniocostadossantos.newsappmvp.local.model.Article

sealed class ArticleListState {
    data class Success(val list: List<Article>) : ArticleListState()
    data class ErrorMessage(val errorMessage: String) : ArticleListState()
    object Loading : ArticleListState()
    object Empty : ArticleListState()
}