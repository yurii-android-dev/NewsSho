package com.yuriishcherbyna.newssho.presentation.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuriishcherbyna.newssho.R
import com.yuriishcherbyna.newssho.domain.model.UserData
import com.yuriishcherbyna.newssho.presentation.account.components.AccountImage
import com.yuriishcherbyna.newssho.presentation.account.components.DeleteAccountConfirmationDialog
import com.yuriishcherbyna.newssho.presentation.account.components.LogOutAndDeleteAccountButtons
import com.yuriishcherbyna.newssho.presentation.components.CenteredTopBar
import com.yuriishcherbyna.newssho.presentation.components.LoadingContent
import com.yuriishcherbyna.newssho.presentation.ui.theme.NewsShoTheme

@Composable
fun AccountScreen(
    userData: UserData?,
    isLoading: Boolean,
    showDialog: Boolean,
    onLogOutClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    changeShowDialogValue: (Boolean) -> Unit
) {

    Scaffold(
        topBar = { CenteredTopBar(nameId = R.string.account) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                LoadingContent(modifier = Modifier.align(Alignment.Center))
            } else {
                AccountBody(
                    userData = userData,
                    onLogOutClicked = onLogOutClicked,
                    onDeleteAccountClicked = { changeShowDialogValue(true) }
                )
            }
        }
        if (showDialog) {
            DeleteAccountConfirmationDialog(
                showDialog = showDialog,
                onDismissRequest = { changeShowDialogValue(false) },
                onCancelClicked = { changeShowDialogValue(false) },
                onConfirmClicked = {
                    onDeleteAccountClicked()
                    changeShowDialogValue(false)
                }
            )
        }
    }
}

@Composable
fun AccountBody(
    userData: UserData?,
    onLogOutClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AccountImage(url = userData?.imageUrl)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = userData?.name ?: stringResource(R.string.no_name),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(16.dp))
        LogOutAndDeleteAccountButtons(
            onLogOutClicked = onLogOutClicked,
            onDeleteAccountClicked = onDeleteAccountClicked
        )
    }
}

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun AccountScreenPreview() {
    NewsShoTheme {
        AccountScreen(
            userData = UserData(
                id = "1",
                imageUrl = "",
                name = "George"
            ),
            isLoading = false,
            showDialog = false,
            onLogOutClicked = {},
            onDeleteAccountClicked = {},
            changeShowDialogValue = {}
        )
    }
}