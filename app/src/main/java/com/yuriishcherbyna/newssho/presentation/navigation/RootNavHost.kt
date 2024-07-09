package com.yuriishcherbyna.newssho.presentation.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yuriishcherbyna.newssho.R
import com.yuriishcherbyna.newssho.presentation.account.AccountScreen
import com.yuriishcherbyna.newssho.presentation.account.AccountViewModel
import com.yuriishcherbyna.newssho.presentation.bookmarks.BookmarksScreen
import com.yuriishcherbyna.newssho.presentation.bookmarks.BookmarksViewModel
import com.yuriishcherbyna.newssho.presentation.details.DetailsScreen
import com.yuriishcherbyna.newssho.presentation.home.HomeScreen
import com.yuriishcherbyna.newssho.presentation.home.HomeViewModel
import com.yuriishcherbyna.newssho.presentation.sign_in.SignInScreen
import com.yuriishcherbyna.newssho.presentation.sign_in.SignInViewModel
import com.yuriishcherbyna.newssho.presentation.util.findActivity
import com.yuriishcherbyna.newssho.presentation.welcome.WelcomeScreen
import com.yuriishcherbyna.newssho.presentation.welcome.WelcomeViewModel
import com.yuriishcherbyna.newssho.util.Constants.DETAILS_SCREEN_TITLE_ARG
import com.yuriishcherbyna.newssho.util.Constants.DETAILS_SCREEN_URL_ARG
import java.net.URLEncoder

@Composable
fun RootNavHost(
    startDestination: String
) {

    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    
    var selectedItem by remember { mutableIntStateOf(0) }
    val showBottomBar by remember {
        derivedStateOf {
            currentBackStackEntry?.destination?.hierarchy?.
                any {
                    it.route == Screens.Welcome.route ||
                    it.route == Screens.SignIn.route ||
                    it.route == Screens.Details.route
                } == false
        }
    }
    val items = listOf(
        Screens.Home,
        Screens.Bookmarks,
        Screens.Account
    )

    val signInScreen = currentBackStackEntry?.destination?.hierarchy?.any {
        it.route == Screens.SignIn.route
    } == true

    if (signInScreen) {
        selectedItem = 0
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    items.forEachIndexed { index, screen ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    painter = painterResource(id = screen.icon),
                                    contentDescription = stringResource(id = screen.label)
                                )
                            },
                            label = { Text(text = stringResource(id = screen.label)) },
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
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
                val activity = context.findActivity()

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
                        signInViewModel.signInWithGoogle(activity)
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
                    onAction = homeViewModel::onAction,
                    onNewsClicked = { url, title ->
                        val encodedUrl = URLEncoder.encode(url, "UTF-8")
                        navController.navigate(Screens.Details.passUrlAndTitle(encodedUrl, title))
                    }
                )
            }
            composable(
                route = Screens.Details.route,
                arguments = listOf(
                    navArgument(DETAILS_SCREEN_URL_ARG) { type = NavType.StringType },
                    navArgument(DETAILS_SCREEN_TITLE_ARG) { type = NavType.StringType }
                )
            ) {

                val url = it.arguments?.getString(DETAILS_SCREEN_URL_ARG)
                val title = it.arguments?.getString(DETAILS_SCREEN_TITLE_ARG)

                DetailsScreen(
                    url = url,
                    title = title,
                    onNavigateBackClicked = { navController.navigateUp() }
                )
            }
            composable(
                route = Screens.Bookmarks.route
            ) {
                val bookmarksViewModel: BookmarksViewModel = hiltViewModel()
                val uiState by bookmarksViewModel.uiState.collectAsState()

                BookmarksScreen(
                    uiState = uiState,
                    onAction = bookmarksViewModel::onAction,
                    onNewsClicked = { url, title ->
                        val encodedUrl = URLEncoder.encode(url, "UTF-8")
                        navController.navigate(Screens.Details.passUrlAndTitle(encodedUrl, title))
                    }
                )
            }
            composable(
                route = Screens.Account.route
            ) {
                val accountViewModel: AccountViewModel = hiltViewModel()
                val userData by accountViewModel.userData.collectAsState()
                val isLoading by accountViewModel.isLoading.collectAsState()

                AccountScreen(
                    userData = userData,
                    isLoading = isLoading,
                    showDialog = accountViewModel.showDialog,
                    onLogOutClicked = {
                        accountViewModel.logOut()
                        navController.navigate(Screens.SignIn.route) {
                            popUpTo(Screens.Account.route) {
                                inclusive = true
                            }
                        }
                    },
                    onDeleteAccountClicked = {
                        accountViewModel.deleteAccount()
                        navController.navigate(Screens.SignIn.route) {
                            popUpTo(Screens.Account.route) {
                                inclusive = true
                            }
                        }
                    },
                    changeShowDialogValue = accountViewModel::changeShowDialogValue
                )
            }
        }
    }

}