package com.antoniocostadossantos.newsappmvp.ui.fragments.home

import android.app.Application
import androidx.lifecycle.*
import com.antoniocostadossantos.newsappmvp.local.model.NewsResponse
import com.antoniocostadossantos.newsappmvp.repository.NewsRepository
import com.antoniocostadossantos.newsappmvp.util.checkForInternetConnection
import com.antoniocostadossantos.newsappmvp.util.state.StateResource
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel constructor(
    private val repository: NewsRepository,
    context: Application
) : AndroidViewModel(context) {

    private val _getAll = MutableLiveData<StateResource<NewsResponse>>()
    val getAll: LiveData<StateResource<NewsResponse>> get() = _getAll

    init {
        safeFetchAll()
    }

    private fun safeFetchAll() = viewModelScope.launch {
        _getAll.value = StateResource.Loading()
        try {
            if (checkForInternetConnection(getApplication())) {
                val response = repository.getAllRemote()
                _getAll.value = handleResponse(response)
            }else{
                _getAll.value = StateResource.Error("Sem conexão com a internet")
            }
        } catch (e: Exception) {
            _getAll.value = StateResource.Error("Artigos não encontrados.")
        }
    }

    private fun handleResponse(response: Response<NewsResponse>): StateResource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { values ->
                return StateResource.Success(values)
            }
        }
        return StateResource.Error(response.message())
    }

}