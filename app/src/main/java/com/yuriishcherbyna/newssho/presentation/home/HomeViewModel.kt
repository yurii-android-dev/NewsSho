package com.yuriishcherbyna.newssho.presentation.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriishcherbyna.newssho.R
import com.yuriishcherbyna.newssho.data.remote.dto.Category
import com.yuriishcherbyna.newssho.domain.model.NewsItem
import com.yuriishcherbyna.newssho.domain.repository.BookmarksNewsRepository
import com.yuriishcherbyna.newssho.domain.repository.NewsRepository
import com.yuriishcherbyna.newssho.domain.util.Result
import com.yuriishcherbyna.newssho.presentation.util.toNetworkErrorMessageId
import com.yuriishcherbyna.newssho.util.Constants.SELECTED_CATEGORY_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val bookmarksNewsRepository: BookmarksNewsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    val selectedCategory = savedStateHandle.getStateFlow(
        SELECTED_CATEGORY_KEY,
        Category.GENERAL
    )

    init {
        getLatestNews(selectedCategory.value)
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.ClearSearchNews -> { clearSearchNews() }
            HomeAction.ClearSearchQuery -> { clearSearchQueryState() }
            HomeAction.ClearErrorMessage -> { clearErrorMessageState() }
            is HomeAction.OnCategoryClicked -> {
                onCategoryChanged(action.category)
                clearLatestNewsState()
                getLatestNews(action.category)
            }
            is HomeAction.OnQueryChanged -> { onSearchQueryChanged(action.query) }
            HomeAction.OnRefreshClicked -> {
                if (_uiState.value.isSearchBarActive) {
                    searchNews()
                } else {
                    clearLatestNewsState()
                    getLatestNews(selectedCategory.value)
                }
            }
            HomeAction.OnSearchBarActiveChanged -> { toogleSearchBarActive() }
            HomeAction.OnSearchClicked -> {}
            is HomeAction.OnBookmarkClicked -> { toogleBookmark(action.newsItem) }
        }
    }

    private fun getLatestNews(category: Category) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            newsRepository.getLatestNews(category).collect { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = result.error.toNetworkErrorMessageId(),
                                isLoading = false
                            )
                        }
                    }

                    is Result.Success -> {
                        _uiState.update { it.copy(news = result.data, isLoading = false) }
                    }
                }
            }
        }
        getBookmarksNews()
    }

    @OptIn(FlowPreview::class)
    private fun searchNews() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            if (_uiState.value.searchQuery.isNotEmpty()) {
                newsRepository.searchNews(_uiState.value.searchQuery)
                    .debounce(500)
                    .collect { result ->
                        when (result) {
                            is Result.Error -> {
                                _uiState.update {
                                    it.copy(
                                        searchErrorMessage = result.error.toNetworkErrorMessageId(),
                                        isLoading = false
                                    )
                                }
                            }

                            is Result.Success -> {
                                _uiState.update {
                                    it.copy(searchNews = result.data, isLoading = false)
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun toogleBookmark(newsItem: NewsItem) {
        viewModelScope.launch {
            when (bookmarksNewsRepository.toogleBookmark(newsItem)) {
                is Result.Error -> {
                    _uiState.update {
                        it.copy(errorMessage = R.string.failed_to_toogle_bookmarks_errors)
                    }
                }
                is Result.Success -> {}
            }
            getBookmarksNews()
        }
    }

    private fun getBookmarksNews() {
        viewModelScope.launch {
            when (val result = bookmarksNewsRepository.getAllNews()) {
                is Result.Error -> {
                    _uiState.update {
                        it.copy(errorMessage = R.string.failed_to_fetch_bookmarks_errors)
                    }
                }
                is Result.Success -> {
                    _uiState.update { it.copy(bookmarksNews = result.data) }
                }
            }
        }
    }

    private fun onCategoryChanged(category: Category) {
        savedStateHandle["selected_category"] = category
    }

    private fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchNews()
    }

    private fun toogleSearchBarActive() {
        _uiState.update { it.copy(isSearchBarActive = !_uiState.value.isSearchBarActive) }
    }

    private fun clearSearchQueryState() {
        _uiState.update { it.copy(searchQuery = "") }
    }

    private fun clearSearchNews() {
        _uiState.update { it.copy(searchNews = emptyList()) }
    }

    private fun clearLatestNewsState() {
        _uiState.update {
            it.copy(
                news = emptyList(),
                errorMessage = null
            )
        }
    }

    private fun clearErrorMessageState() { _uiState.update { it.copy(errorMessage = null) } }

}