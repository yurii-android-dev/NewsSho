package com.yuriishcherbyna.newssho.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriishcherbyna.newssho.data.remote.dto.Category
import com.yuriishcherbyna.newssho.domain.repository.NewsRepository
import com.yuriishcherbyna.newssho.domain.util.Result
import com.yuriishcherbyna.newssho.presentation.util.toNetworkErrorMessageId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getLatestNews(Category.GENERAL)
    }

    fun getLatestNews(category: Category) {
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

}