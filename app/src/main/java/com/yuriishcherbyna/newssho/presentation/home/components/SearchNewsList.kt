package com.yuriishcherbyna.newssho.presentation.home.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.yuriishcherbyna.newssho.domain.model.NewsItem
import com.yuriishcherbyna.newssho.presentation.components.NewsCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchNewsList(
    searchNews: LazyPagingItems<NewsItem>,
    bookmarksNews: List<NewsItem>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onNewsClicked: (String, String) -> Unit,
    onBookmarkClicked: (NewsItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            count = searchNews.itemCount,
        ) { index ->

            val item = searchNews[index]

            val isBookmarked = bookmarksNews.find { it.url == item?.url }

            item?.let {
                NewsCard(
                    newsItem = item,
                    isBookmarked = isBookmarked != null,
                    onNewsClicked = onNewsClicked,
                    onBookmarkClicked = onBookmarkClicked,
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
    }
}