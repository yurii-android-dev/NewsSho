package com.yuriishcherbyna.newssho.presentation.components

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.yuriishcherbyna.newssho.R

@Composable
fun ConfirmationDialog(
    @StringRes titleText: Int,
    @StringRes descriptionText: Int,
    onDismissRequest: () -> Unit,
    onCancelClicked: () -> Unit,
    onConfirmClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(id = titleText)) },
        text = { Text(text = stringResource(id = descriptionText)) },
        dismissButton = {
            TextButton(onClick = onCancelClicked) {
                Text(text = stringResource(R.string.cancel).uppercase())
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirmClicked) {
                Text(text = stringResource(R.string.confirm).uppercase())
            }
        }
    )
}