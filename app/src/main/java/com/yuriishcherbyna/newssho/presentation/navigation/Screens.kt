package com.yuriishcherbyna.newssho.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.yuriishcherbyna.newssho.R
import com.yuriishcherbyna.newssho.util.Constants.DETAILS_SCREEN_TITLE_ARG
import com.yuriishcherbyna.newssho.util.Constants.DETAILS_SCREEN_URL_ARG

sealed class Screens(
    val route: String,
    @StringRes val label: Int,
    @DrawableRes val icon: Int
) {

    data object Welcome : Screens(
        route = "welcome_screen",
        label = -1,
        icon = -1
    )
    data object SignIn : Screens(
        route = "sign_in_screen",
        label = -1,
        icon = -1
    )

    data object Home : Screens(
        route = "home_screen",
        label = R.string.home_label,
        icon = R.drawable.ic_home_24
    )

    data object Details :
        Screens(
            route = "details_screen/{$DETAILS_SCREEN_URL_ARG}/{$DETAILS_SCREEN_TITLE_ARG}",
            label = -1,
            icon = -1
        ) {
        fun passUrlAndTitle(url: String, title: String?) = "details_screen/$url/$title"
    }

    data object Bookmarks : Screens(
        route = "bookmarks_screen",
        label = R.string.bookmarks_label,
        icon = R.drawable.ic_bookmark_24
    )

    data object Account : Screens(
        route = "profile_screen",
        label = R.string.account_label,
        icon = R.drawable.ic_account_circle_24
    )

}