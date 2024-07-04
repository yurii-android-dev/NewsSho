package com.yuriishcherbyna.newssho.presentation.bookmarks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuriishcherbyna.newssho.R
import com.yuriishcherbyna.newssho.presentation.bookmarks.components.EmptyContent
import com.yuriishcherbyna.newssho.presentation.components.CenteredTopBar
import com.yuriishcherbyna.newssho.presentation.components.LoadingContent
import com.yuriishcherbyna.newssho.presentation.components.NewsList
import com.yuriishcherbyna.newssho.presentation.ui.theme.NewsShoTheme
import kotlinx.coroutines.launch

@Composable
fun BookmarksScreen(
    uiState: BookmarksUiState,
    onAction: (BookmarksAction) -> Unit,
    onNewsClicked: (String,String) -> Unit,
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    LaunchedEffect(key1 = uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            snackBarHostState.showSnackbar(message = context.getString(uiState.errorMessage))
        }
    }

    Scaffold(
        topBar = { CenteredTopBar(nameId = R.string.bookmarks) },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState)},
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.bookmarksNews.isNotEmpty() -> {
                    NewsList(
                        news = uiState.bookmarksNews,
                        bookmarksNews = uiState.bookmarksNews,
                        onNewsClicked = onNewsClicked,
                        onBookmarkClicked = { newsItem ->
                            onAction(BookmarksAction.OnBookmarkClicked(newsItem))
                            coroutineScope.launch {
                                val result = snackBarHostState.showSnackbar(
                                    message = context.getString(
                                        R.string.bookmark_successfully_deleted
                                    ),
                                    duration = SnackbarDuration.Short,
                                    actionLabel = context.getString(R.string.undo)
                                )
                                when (result) {
                                    SnackbarResult.ActionPerformed -> {
                                        onAction(BookmarksAction.OnUndoClicked(newsItem))
                                    }
                                    SnackbarResult.Dismissed -> {}
                                }
                            }
                        }
                    )
                }
                uiState.isLoading -> { LoadingContent(modifier = Modifier.align(Alignment.Center)) }
                else -> { EmptyContent(modifier = Modifier.align(Alignment.Center)) }
             }
        }
    }
}

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun BookmarksScreenPreview() {
    NewsShoTheme {
        BookmarksScreen(
            uiState = BookmarksUiState(errorMessage = R.string.unknown_error),
            onAction = {},
            onNewsClicked = {_, _ ->}
        )
    }
}