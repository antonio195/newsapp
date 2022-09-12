package com.antoniocostadossantos.newsappmvp.util.state

sealed class ArticleListEvent {
    object Fetch : ArticleListEvent()
}