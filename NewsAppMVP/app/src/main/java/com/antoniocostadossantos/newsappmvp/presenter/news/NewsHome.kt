package com.antoniocostadossantos.newsappmvp.presenter.news

import com.antoniocostadossantos.newsappmvp.local.model.NewsResponse

interface NewsHome {

    interface Presenter {
        fun requestAll()
        fun onSucess(newsResponse: NewsResponse)
        fun onError(message: String)
        fun onComplete()
    }
}