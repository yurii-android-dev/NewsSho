package com.yuriishcherbyna.newssho.presentation.account.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.yuriishcherbyna.newssho.R

@Composable
fun AccountImage(
    url: String?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = url,
        contentDescription = stringResource(R.string.account_image),
        error = painterResource(id = R.drawable.ic_placeholder),
        modifier = modifier
            .clip(CircleShape)
            .size(150.dp)
    )
}