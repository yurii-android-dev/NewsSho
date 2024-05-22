package com.yuriishcherbyna.newssho.presentation.navigation

sealed class Screens(val route: String) {

    data object Welcome: Screens("welcome_screen")
    data object SignIn: Screens("sign_in_screen")

}