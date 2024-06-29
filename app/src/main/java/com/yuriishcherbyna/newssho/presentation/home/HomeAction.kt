package com.yuriishcherbyna.newssho.presentation.home

import com.yuriishcherbyna.newssho.data.remote.dto.Category
import com.yuriishcherbyna.newssho.domain.model.NewsItem

sealed class HomeAction {

    data class OnQueryChanged(val query: String): HomeAction()

    data object OnSearchBarActiveChanged : HomeAction()

    data object ClearSearchQuery: HomeAction()

    data object ClearSearchNews: HomeAction()

    data object ClearErrorMessage: HomeAction()

    data object OnSearchClicked: HomeAction()

    data class OnCategoryClicked(val category: Category): HomeAction()

    data object OnRefreshClicked: HomeAction()

    data class OnBookmarkClicked(val newsItem: NewsItem): HomeAction()

}