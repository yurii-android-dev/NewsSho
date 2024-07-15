package com.yuriishcherbyna.newssho.presentation.bookmarks

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuriishcherbyna.newssho.R
import com.yuriishcherbyna.newssho.presentation.bookmarks.components.EmptyContent
import com.yuriishcherbyna.newssho.presentation.components.ConfirmationDialog
import com.yuriishcherbyna.newssho.presentation.components.LoadingContent
import com.yuriishcherbyna.newssho.presentation.components.NewsList
import com.yuriishcherbyna.newssho.presentation.ui.theme.NewsShoTheme
import kotlinx.coroutines.launch

@Composable
fun BookmarksScreen(
    uiState: BookmarksUiState,
    showDialog: Boolean,
    onAction: (BookmarksAction) -> Unit,
    onNewsClicked: (String,String) -> Unit,
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    LaunchedEffect(key1 = uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            snackBarHostState.showSnackbar(message = context.getString(uiState.errorMessage))
            onAction(BookmarksAction.ClearErrorMessageState)
        }
    }

    Scaffold(
        topBar = {
            BookmarksTopBar(
                nameId = R.string.bookmarks,
                onDeleteBookmarksClicked = { onAction(BookmarksAction.ToogleShowDialogValue) }
            )
        },
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

        if (showDialog) {
            ConfirmationDialog(
                titleText = R.string.delete_bookmarks_alert_dialog_title,
                descriptionText = R.string.delete_account_alert_dialog_text,
                onDismissRequest = { onAction(BookmarksAction.ToogleShowDialogValue) },
                onCancelClicked = { onAction(BookmarksAction.ToogleShowDialogValue) },
                onConfirmClicked = { onAction(BookmarksAction.ConfirmButtonClicked) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksTopBar(
    @StringRes nameId: Int,
    onDeleteBookmarksClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = nameId),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = onDeleteBookmarksClicked) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_icon)
                )
            }
        }
    )
}

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun BookmarksScreenPreview() {
    NewsShoTheme {
        BookmarksScreen(
            uiState = BookmarksUiState(errorMessage = R.string.unknown_error),
            showDialog = false,
            onAction = {},
            onNewsClicked = {_, _ ->}
        )
    }
}