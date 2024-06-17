package com.yuriishcherbyna.newssho.presentation.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuriishcherbyna.newssho.R
import com.yuriishcherbyna.newssho.data.remote.dto.Category
import com.yuriishcherbyna.newssho.domain.model.NewsItem
import com.yuriishcherbyna.newssho.presentation.components.ErrorContent
import com.yuriishcherbyna.newssho.presentation.components.LoadingContent
import com.yuriishcherbyna.newssho.presentation.home.components.Chip
import com.yuriishcherbyna.newssho.presentation.home.components.NewsCard
import com.yuriishcherbyna.newssho.presentation.ui.theme.NewsShoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    selectedCategory: Category,
    onRefreshClicked: () -> Unit,
    onNewsClicked: () -> Unit,
    onBookmarkClicked: () -> Unit,
    onCategoryClicked: (Category) -> Unit,
    onSearchClicked: () -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            snackbarHostState.showSnackbar(
                message = context.getString(uiState.errorMessage),
                duration = SnackbarDuration.Long
            )
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                CenteredHomeTopBar(
                    scrollBehavior = scrollBehavior,
                    onSearchClicked = onSearchClicked
                )
                if (uiState.errorMessage == null) {
                    CategoryChips(
                        selectedCategory = selectedCategory,
                        onCategoryClicked = onCategoryClicked
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.news.isNotEmpty()) {
                NewsList(
                    news = uiState.news,
                    onNewsClicked = onNewsClicked,
                    onBookmarkClicked = onBookmarkClicked
                )
            } else if (uiState.isLoading) {
                LoadingContent(modifier = Modifier.align(Alignment.Center))
            } else {
                ErrorContent(onRefreshClicked = onRefreshClicked)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredHomeTopBar(
    onSearchClicked: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        },
        scrollBehavior = scrollBehavior,
        actions = {
            IconButton(onClick = onSearchClicked) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_icon),
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }
    )
}

@Composable
fun CategoryChips(
    selectedCategory: Category,
    onCategoryClicked: (Category) -> Unit,
    modifier: Modifier = Modifier
) {

    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .horizontalScroll(scrollState)
    ) {
        Category.entries.forEach { category ->
            Chip(
                selectedCategory = selectedCategory,
                text = category.name,
                onCategoryClicked = {
                    onCategoryClicked(category)
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun NewsList(
    news: List<NewsItem>,
    onNewsClicked: () -> Unit,
    onBookmarkClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = news,
            key = { newsItem ->
                newsItem.url + newsItem.publishedAt
            }
        ) {newsResult ->
            NewsCard(
                newsItem = newsResult,
                isBookmarked = false,
                onNewsClicked = onNewsClicked,
                onBookmarkClicked = onBookmarkClicked
            )
        }
    }
}

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun HomeScreenPreview() {
    NewsShoTheme {
        val news = List(5) {
            NewsItem(
                title = "McDonald's loses right to chicken Big Mac name",
                url = "https://www.bbc.co.uk/news/articles/cp00jj7ze3qo + $it",
                urlToImage = "https://ichef.bbci.co.uk/news/1024/branded_news/c4f0/live/602a1790-2328-11ef-b04e-375672a6fc86.jpg",
                publishedAt = "2024-06-07T06:07:21.6929534Z",
                sourceName = "BBC News"
            )
        }
        HomeScreen(
            uiState = HomeUiState(news = news),
            selectedCategory = Category.GENERAL,
            onRefreshClicked = {},
            onNewsClicked = {},
            onBookmarkClicked = {},
            onCategoryClicked = {},
            onSearchClicked = {}
        )
    }
}