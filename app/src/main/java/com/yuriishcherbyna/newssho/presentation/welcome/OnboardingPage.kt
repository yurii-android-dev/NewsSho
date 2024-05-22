package com.yuriishcherbyna.newssho.presentation.welcome

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.yuriishcherbyna.newssho.R
import com.yuriishcherbyna.newssho.presentation.ui.theme.SoftDarkBlue
import com.yuriishcherbyna.newssho.presentation.ui.theme.SoftDarkGreen
import com.yuriishcherbyna.newssho.presentation.ui.theme.SoftDarkPurple

sealed class OnboardingPage(
    val color: Color,
    @RawRes
    val icon: Int,
    @StringRes
    val title: Int
) {
    data object First: OnboardingPage(
        color = SoftDarkBlue,
        icon = R.raw.first_screen_anim,
        title = R.string.first_onboarding_title,
    )

    data object Second: OnboardingPage(
        color = SoftDarkGreen,
        icon = R.raw.second_screen_anim,
        title = R.string.second_onboarding_title,
    )

    data object Third: OnboardingPage(
        color = SoftDarkPurple,
        icon = R.raw.third_screen_anim,
        title = R.string.third_onboarding_title,
    )
}