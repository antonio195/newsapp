package com.antoniocostadossantos.newsappmvp.presenter.favorite

import com.antoniocostadossantos.newsappmvp.local.model.Article

interface FavoriteHome {

    interface Presenter{
        fun onSucess(article: List<Article>)
    }

}