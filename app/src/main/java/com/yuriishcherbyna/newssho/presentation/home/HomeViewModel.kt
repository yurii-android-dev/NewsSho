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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    val selectedCategory = savedStateHandle.getStateFlow(
        SELECTED_CATEGORY_KEY,
        Category.GENERAL
    )

    init {
        getLatestNews(selectedCategory.value)
    }

    fun getLatestNews(category: Category) {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            newsRepository.getLatestNews(category).collect { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.value = HomeUiState.Error(
                            error = result.error.toNetworkErrorMessageId()
                        )
                    }
                    is Result.Success -> {
                       val news = result.data
                       _uiState.value = HomeUiState.Success(news)
                    }
                }
            }
        }
    }

    fun onCategoryChanged(category: Category) {
        savedStateHandle["selected_category"] = category
    }

}