package com.mydigipay.challenge.ui.search.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mydigipay.challenge.base.BaseViewModel
import com.mydigipay.challenge.data.network.ApiResult
import com.mydigipay.challenge.data.pojo.Repository
import com.mydigipay.challenge.utils.ktx.debounce
import com.mydigipay.challenge.utils.ktx.logD
import kotlinx.coroutines.launch

class SearchViewModel(private val model: SearchModel) : BaseViewModel(model) {


    private val _repositories = MutableLiveData<List<Repository>>()
    val repositories: LiveData<List<Repository>>
        get() = _repositories

    private val isLoading = MutableLiveData<Boolean>(false)


    val query = MutableLiveData<String>()

    val onQueryChanged = query.debounce(500L)

    init {
        searchRepositories(1)
    }

    fun searchRepositories(page: Int) = viewModelScope.launch {
        query.value?.trim().takeIf { !it.isNullOrBlank() }?.let { query ->
            isLoading.postValue(true)
            val repos =
                if (page == 1)
                    mutableListOf()
                else
                    (_repositories.value ?: emptyList()).toMutableList()

            when (val result = model.searchRepository(query, page)) {
                is ApiResult.Success -> {
                    repos.addAll(result.data)
                    _repositories.postValue(repos)
                }
                is ApiResult.Error -> {
                    logD("error = ${result.message}")
                }
            }
            isLoading.postValue(false)
        } ?: {
            _repositories.postValue(emptyList())
        }()
    }

    fun goToDetail(repository: Repository) {
        navigateTo(SearchFragmentDirections.actionSearchToDetail(repository))
    }

}