package com.yuriishcherbyna.newssho.presentation.bookmarks

import androidx.annotation.StringRes
import com.yuriishcherbyna.newssho.domain.model.NewsItem

data class BookmarksUiState(
    val isLoading: Boolean = false,
    val bookmarksNews: List<NewsItem> = emptyList(),
    @StringRes val errorMessage: Int? = null
)
