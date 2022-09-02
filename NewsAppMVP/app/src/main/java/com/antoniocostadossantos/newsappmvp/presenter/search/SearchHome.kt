package com.antoniocostadossantos.newsappmvp.presenter.search

import com.antoniocostadossantos.newsappmvp.local.model.NewsResponse

interface SearchHome {

    interface Presenter{
        fun search(term: String)
        fun onSucess(newsResponse: NewsResponse)
        fun onError(message: String)
        fun onComplete()
    }
}