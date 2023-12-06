package com.zoho.news.screens.news.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.zoho.news.database.NewsDatabase
import com.zoho.news.database.NewsEntity
import com.zoho.news.remote.mappers.toNews
import com.zoho.news.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class NewsViewModel @Inject constructor(
    pager: Pager<Int, NewsEntity>,
    newsDb: NewsDatabase
) : ViewModel() {
    private val newsPagingFlow = pager.flow.map { data ->
        data.map { it.toNews() }
    }.cachedIn(viewModelScope)

    private val _searchText = MutableStateFlow(Constants.EMPTY_STRING)
    val searchText = _searchText.asStateFlow()

    private val _news = MutableStateFlow(newsPagingFlow)
    val news = _searchText.debounce(1000L).combine(_news) { text, _ ->
        if (text.isBlank()) newsPagingFlow
        else {
            Pager(PagingConfig(Constants.DEFAULT_PAGE_SIZE)) {
                newsDb.dao.getAllNews(text)
            }.flow.map { data ->
                data.map { it.toNews() }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), _news.value)

    fun searchNews(queryText: String) {
        _searchText.value = queryText
    }

    fun clearSearchText() {
        _searchText.value = Constants.EMPTY_STRING
    }

}