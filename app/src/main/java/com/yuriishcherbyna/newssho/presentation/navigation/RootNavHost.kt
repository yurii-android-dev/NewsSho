package com.yuriishcherbyna.newssho.presentation.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yuriishcherbyna.newssho.R
import com.yuriishcherbyna.newssho.presentation.home.HomeScreen
import com.yuriishcherbyna.newssho.presentation.home.HomeViewModel
import com.yuriishcherbyna.newssho.presentation.sign_in.SignInScreen
import com.yuriishcherbyna.newssho.presentation.sign_in.SignInViewModel
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

            val signInViewModel: SignInViewModel = hiltViewModel()
            val uiState by signInViewModel.uiState.collectAsState()

            val context = LocalContext.current

            LaunchedEffect(key1 = Unit) {
                if (signInViewModel.getSignedInUser() != null) {
                    navController.navigate(Screens.Home.route) {
                        popUpTo(Screens.SignIn.route) {
                            inclusive = true
                        }
                    }
                }
            }

            LaunchedEffect(key1 = uiState.isLoggedIn) {
                if (uiState.isLoggedIn) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.sign_in_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate(Screens.Home.route) {
                        popUpTo(Screens.SignIn.route) {
                            inclusive = true
                        }
                    }
                    signInViewModel.resetState()
                }
            }

            SignInScreen(
                uiState = uiState,
                onSignInClick = {
                    signInViewModel.signInWithGoogle()
                }
            )
        }
        composable(
            route = Screens.Home.route
        ) {

            val homeViewModel: HomeViewModel = hiltViewModel()
            val homeUiState by homeViewModel.uiState.collectAsState()
            val selectedCategory by homeViewModel.selectedCategory.collectAsState()

            HomeScreen(
                uiState = homeUiState,
                selectedCategory = selectedCategory,
                onCategoryClicked = { category ->
                    homeViewModel.onCategoryChanged(category)
                    homeViewModel.clearState()
                    homeViewModel.getLatestNews(category)
                },
                onRefreshClicked = {
                    homeViewModel.clearState()
                    homeViewModel.getLatestNews(selectedCategory)
                },
                onNewsClicked = {},
                onBookmarkClicked = {},
                onSearchClicked = {}
            )
        }
    }

}