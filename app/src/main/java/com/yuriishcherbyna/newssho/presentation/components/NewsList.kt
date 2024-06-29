package com.yuriishcherbyna.newssho.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuriishcherbyna.newssho.domain.model.NewsItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsList(
    news: List<NewsItem>,
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
            items = news,
            key = { newsItem ->
                newsItem.url
            }
        ) { newsResult ->

            val isBookmarked = bookmarksNews.find { it.url == newsResult.url }

            NewsCard(
                newsItem = newsResult,
                isBookmarked = isBookmarked != null,
                onNewsClicked = onNewsClicked,
                onBookmarkClicked = onBookmarkClicked,
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}