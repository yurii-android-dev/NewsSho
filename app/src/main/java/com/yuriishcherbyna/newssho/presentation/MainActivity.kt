package com.yuriishcherbyna.newssho.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.yuriishcherbyna.newssho.domain.repository.DataStoreRepository
import com.yuriishcherbyna.newssho.presentation.navigation.RootNavHost
import com.yuriishcherbyna.newssho.presentation.navigation.Screens
import com.yuriishcherbyna.newssho.presentation.ui.theme.NewsShoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isCompleted = false

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            NewsShoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootNavHost(
                        startDestination = if (isCompleted) Screens.SignIn.route
                            else Screens.Welcome.route
                    )
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreRepository.readOnboardingState().collect { value ->
                isCompleted = value
            }
        }
    }
}