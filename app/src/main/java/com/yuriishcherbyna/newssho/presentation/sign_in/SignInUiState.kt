package com.yuriishcherbyna.newssho.presentation.sign_in

data class SignInUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null
)