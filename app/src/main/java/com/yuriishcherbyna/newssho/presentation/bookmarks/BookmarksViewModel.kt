package com.yuriishcherbyna.newssho.presentation.bookmarks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriishcherbyna.newssho.R
import com.yuriishcherbyna.newssho.domain.model.NewsItem
import com.yuriishcherbyna.newssho.domain.repository.BookmarksNewsRepository
import com.yuriishcherbyna.newssho.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val bookmarksNewsRepository: BookmarksNewsRepository
): ViewModel(){

    private val _uiState = MutableStateFlow(BookmarksUiState())
    val uiState = _uiState.asStateFlow()

    var showDialog by mutableStateOf(false)
        private set

    init {
        getBookmarksNews()
    }

    fun onAction(bookmarksAction: BookmarksAction) {
        when (bookmarksAction) {
            is BookmarksAction.OnBookmarkClicked -> {
                bookmarksAction.newsItem.id?.let { deleteBookmark(it) }
            }
            is BookmarksAction.OnUndoClicked -> { saveNews(bookmarksAction.newsItem) }
            BookmarksAction.DeleteBookmarksClicked -> {
                toogleShowDialogValue()
            }
            BookmarksAction.ConfirmButtonClicked -> {
                toogleShowDialogValue()
                deleteBookmarks()
            }
            BookmarksAction.ToogleShowDialogValue -> { toogleShowDialogValue() }
            BookmarksAction.ClearErrorMessageState -> { clearErrorMessageState() }
        }
    }

    private fun getBookmarksNews() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = bookmarksNewsRepository.getAllNews()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(bookmarksNews = result.data, isLoading = false)
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(errorMessage = R.string.unknown_error, isLoading = false)
                    }
                }
            }
        }
    }

    private fun saveNews(newsItem: NewsItem) {
        bookmarksNewsRepository.saveNews(newsItem)
        getBookmarksNews()
    }

    private fun deleteBookmark(bookmarkId: String) {
        viewModelScope.launch {
            bookmarksNewsRepository.deleteNews(bookmarkId)
            getBookmarksNews()
        }
    }

    private fun deleteBookmarks() {
        viewModelScope.launch {
            if (_uiState.value.bookmarksNews.isNotEmpty()) {
                when (val result = bookmarksNewsRepository.deleteAllNews(20)) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(errorMessage = R.string.bookmarks_successfully_deleted)
                        }
                        getBookmarksNews()
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(errorMessage = R.string.unknown_error)
                        }
                    }
                }
            } else {
                _uiState.update {
                    it.copy(errorMessage = R.string.empty_bookmarks_message)
                }
            }
        }
    }

    private fun toogleShowDialogValue() {
        showDialog = !showDialog
    }

    private fun clearErrorMessageState() {
        _uiState.update { it.copy(errorMessage = null) }
    }

}