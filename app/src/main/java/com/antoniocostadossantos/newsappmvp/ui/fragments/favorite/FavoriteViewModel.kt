package com.antoniocostadossantos.newsappmvp.ui.fragments.favorite

import androidx.lifecycle.*
import com.antoniocostadossantos.newsappmvp.local.model.Article
import com.antoniocostadossantos.newsappmvp.repository.NewsRepository
import com.antoniocostadossantos.newsappmvp.util.state.ArticleListEvent
import com.antoniocostadossantos.newsappmvp.util.state.ArticleListState
import kotlinx.coroutines.launch

class FavoriteViewModel constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _favorite = MutableLiveData<ArticleListEvent>()
    val favorite: LiveData<ArticleListState> = _favorite.switchMap {
        when (it) {
            ArticleListEvent.Fetch -> getAll()
        }
    }

    fun dispatch(event: ArticleListEvent){
        this._favorite.postValue(event)
    }

    private fun getAll(): LiveData<ArticleListState> {
        return liveData {
            try {
                emit(ArticleListState.Loading)
                val listLiveData = repository.getAll()
                    .map { list ->
                        if (list.isEmpty()) {
                            ArticleListState.Empty
                        } else {
                            ArticleListState.Success(list)
                        }
                    }
                emitSource(listLiveData)
            } catch (e: Exception) {
                emit(ArticleListState.ErrorMessage("Algo deu errado."))
            }
        }
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.updateInsert(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.delete(article)
    }

}