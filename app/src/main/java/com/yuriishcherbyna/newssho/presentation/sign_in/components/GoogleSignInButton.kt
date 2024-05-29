package com.yuriishcherbyna.newssho.presentation.sign_in.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yuriishcherbyna.newssho.R
import com.yuriishcherbyna.newssho.presentation.ui.theme.DarkGray
import com.yuriishcherbyna.newssho.presentation.ui.theme.LightGray

@Composable
fun GoogleSignInButton(
    isLoading: Boolean,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onSignInClick,
        modifier = modifier
            .height(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LightGray
        ),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.animateContentSize()
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(20.dp)
                    .paint(painter = painterResource(id = R.drawable.google_logo))
            )
            Text(
                text = stringResource(if (isLoading) R.string.sign_in_with_google_loading
                else R.string.sign_in_with_google
                ),
                color = DarkGray
            )
            AnimatedVisibility(visible = isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(20.dp)
                )
            }
        }
    }
}