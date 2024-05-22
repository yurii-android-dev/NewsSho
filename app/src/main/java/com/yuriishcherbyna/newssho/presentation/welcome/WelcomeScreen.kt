package com.yuriishcherbyna.newssho.presentation.welcome

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yuriishcherbyna.newssho.R
import com.yuriishcherbyna.newssho.presentation.ui.theme.NewsShoTheme
import com.yuriishcherbyna.newssho.presentation.welcome.components.PagingIndicatorAndButtonControllers
import com.yuriishcherbyna.newssho.util.Constants.ONBOARDING_TOTAL_PAGES

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeScreen(
    onDoneClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val pages = listOf(
        OnboardingPage.First,
        OnboardingPage.Second,
        OnboardingPage.Third
    )

    val pagerState = rememberPagerState(pageCount = { ONBOARDING_TOTAL_PAGES })

    val backgroundColor by animateColorAsState(
        targetValue = pages[pagerState.currentPage].color,
        label = stringResource(R.string.pager_background_color_label),
        animationSpec = tween(
            durationMillis = 500
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) {
            OnboardingPage(
                page = pages[pagerState.currentPage]
            )
        }
        PagingIndicatorAndButtonControllers(
            pagerState = pagerState,
            onDoneClicked = onDoneClicked,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 40.dp, horizontal = 16.dp)
        )
    }
}
@Composable
fun OnboardingPage(
    page: OnboardingPage,
    modifier: Modifier = Modifier
) {

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(page.icon)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .height(250.dp)
                    .width(250.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(id = page.title),
                fontSize = 26.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}



@Preview(showBackground = true, apiLevel = 33)
@Composable
fun WelcomeScreenPreview() {
    NewsShoTheme {
        WelcomeScreen(
            onDoneClicked = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FirstOnboardingPagePreview() {
    OnboardingPage(
        page = OnboardingPage.First
    )
}

@Preview(showBackground = true)
@Composable
fun SecondOnboardingPagePreview() {
    OnboardingPage(
        page = OnboardingPage.Second
    )
}

@Preview(showBackground = true)
@Composable
fun ThirdOnboardingPagePreview() {
    OnboardingPage(
        page = OnboardingPage.Third
    )
}

