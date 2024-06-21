package com.yuriishcherbyna.newssho.presentation.navigation

import com.yuriishcherbyna.newssho.util.Constants.DETAILS_SCREEN_TITLE_ARG
import com.yuriishcherbyna.newssho.util.Constants.DETAILS_SCREEN_URL_ARG

sealed class Screens(val route: String) {

    data object Welcome : Screens("welcome_screen")
    data object SignIn : Screens("sign_in_screen")

    data object Home : Screens("home_screen")

    data object Details :
        Screens("details_screen/{$DETAILS_SCREEN_URL_ARG}/{$DETAILS_SCREEN_TITLE_ARG}") {
        fun passUrlAndTitle(url: String, title: String) = "details_screen/$url/$title"
    }

}