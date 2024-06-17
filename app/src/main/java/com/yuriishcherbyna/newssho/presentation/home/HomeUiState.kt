package com.yuriishcherbyna.newssho.presentation.home

import androidx.annotation.StringRes
import com.yuriishcherbyna.newssho.domain.model.NewsItem

data class HomeUiState(
    val news: List<NewsItem> = emptyList(),
    val isLoading: Boolean = false,
    @StringRes val errorMessage: Int? = null
)