package com.yuriishcherbyna.newssho.presentation.account.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yuriishcherbyna.newssho.R

@Composable
fun LogOutAndDeleteAccountButtons(
    onLogOutClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Button(
            onClick = onLogOutClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.logout))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onDeleteAccountClicked() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.delete_account))
        }
    }
}