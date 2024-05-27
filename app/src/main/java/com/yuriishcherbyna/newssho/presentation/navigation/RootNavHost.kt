package com.yuriishcherbyna.newssho.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yuriishcherbyna.newssho.presentation.sign_in.SignInScreen
import com.yuriishcherbyna.newssho.presentation.sign_in.SignInUiState
import com.yuriishcherbyna.newssho.presentation.welcome.WelcomeScreen
import com.yuriishcherbyna.newssho.presentation.welcome.WelcomeViewModel

@Composable
fun RootNavHost(
    startDestination: String
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screens.Welcome.route
        ) {

            val welcomeViewModel: WelcomeViewModel = hiltViewModel()

            WelcomeScreen(
                onDoneClicked = {
                    welcomeViewModel.saveOnboardingState(true)
                    navController.navigate(Screens.SignIn.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = Screens.SignIn.route,
        ) {
            SignInScreen(
                uiState = SignInUiState(),
                onSignInClick = {}
            )
        }
    }

}