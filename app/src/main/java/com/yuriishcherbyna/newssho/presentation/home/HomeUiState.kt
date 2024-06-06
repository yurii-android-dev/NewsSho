package com.yuriishcherbyna.newssho.presentation.home

import androidx.annotation.StringRes
import com.yuriishcherbyna.newssho.domain.model.NewsItem

sealed interface HomeUiState {
    data class Success(val news: List<NewsItem>): HomeUiState
    data object Loading: HomeUiState
    data class Error(@StringRes val error: Int): HomeUiState
}