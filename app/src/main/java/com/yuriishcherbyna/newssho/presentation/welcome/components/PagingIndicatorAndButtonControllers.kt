package com.yuriishcherbyna.newssho.presentation.welcome.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yuriishcherbyna.newssho.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagingIndicatorAndButtonControllers(
    pagerState: PagerState,
    onDoneClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        if (pagerState.currentPage > 0) {
            Text(
                text = stringResource(id = R.string.back),
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                page = pagerState.currentPage - 1,
                                animationSpec = tween(
                                    durationMillis = 500
                                )
                            )
                        }
                    }
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .clip(CircleShape)
                    .size(8.dp)
                    .background(color)
                    .align(Alignment.CenterVertically)
            )
        }
        if (pagerState.currentPage < pagerState.pageCount - 1) {
            Text(
                text = stringResource(id = R.string.next),
                textAlign = TextAlign.End,
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                page = pagerState.currentPage + 1,
                                animationSpec = tween(
                                    durationMillis = 500
                                )
                            )
                        }
                    }
            )
        } else {
            Text(
                text = stringResource(id = R.string.done),
                textAlign = TextAlign.End,
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onDoneClicked()
                    }
            )
        }
    }
}