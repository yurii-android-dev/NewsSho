package com.yuriishcherbyna.newssho.presentation.bookmarks

import com.yuriishcherbyna.newssho.domain.model.NewsItem

sealed class BookmarksAction {

    data class OnBookmarkClicked(val newsItem: NewsItem): BookmarksAction()

    data class OnUndoClicked(val newsItem: NewsItem): BookmarksAction()

    data object DeleteBookmarksClicked: BookmarksAction()

    data object ToogleShowDialogValue: BookmarksAction()

    data object ConfirmButtonClicked: BookmarksAction()

    data object ClearErrorMessageState: BookmarksAction()

}