package com.yuriishcherbyna.newssho.presentation.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriishcherbyna.newssho.data.remote.dto.Category
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

    fun getLatestNews(category: Category) {
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
    }

    @OptIn(FlowPreview::class)
    fun searchNews() {
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

    fun onCategoryChanged(category: Category) {
        savedStateHandle["selected_category"] = category
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchNews()
    }

    fun toogleSearchBarActive() {
        _uiState.update { it.copy(isSearchBarActive = !_uiState.value.isSearchBarActive) }
    }

    fun clearSearchQueryState() {
        _uiState.update { it.copy(searchQuery = "") }
    }

    fun clearSearchNews() {
        _uiState.update { it.copy(searchNews = emptyList()) }
    }

    fun clearLatestNewsState() {
        _uiState.update {
            it.copy(
                news = emptyList(),
                errorMessage = null
            )
        }
    }

}