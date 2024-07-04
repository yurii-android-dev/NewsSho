package com.yuriishcherbyna.newssho.presentation.account.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.yuriishcherbyna.newssho.R

@Composable
fun DeleteAccountConfirmationDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onCancelClicked: () -> Unit,
    onConfirmClicked: () -> Unit
) {

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = stringResource(id = R.string.delete_account_alert_dialog_title)) },
            text = { Text(text = stringResource(id = R.string.delete_account_alert_dialog_text)) },
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
}