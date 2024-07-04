package com.yuriishcherbyna.newssho.presentation.components

import androidx.annotation.StringRes
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.yuriishcherbyna.newssho.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredTopBar(@StringRes nameId: Int) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = nameId),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }
    )
}