package com.yuriishcherbyna.newssho.presentation.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriishcherbyna.newssho.domain.model.SignInResult
import com.yuriishcherbyna.newssho.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    private fun onSignedInResult(result: SignInResult) {
        _uiState.update { state ->
            state.copy(
                isLoggedIn = result.userData != null,
                errorMessage = result.error
            )
        }
    }

    fun getSignedInUser() = authRepository.getUserData()

    fun signInWithGoogle() {
        _uiState.update { state -> state.copy(isLoading = true) }
        viewModelScope.launch {
            val tokenResult = authRepository.getGoogleToken()
            tokenResult?.let { token ->
                val result = authRepository.signIn(token)
                onSignedInResult(result)
                _uiState.update { state -> state.copy(isLoading = false) }
            } ?: run {
                _uiState.update { state -> state.copy(isLoading = false) }
            }
        }
    }

    fun resetState() = _uiState.update { SignInUiState() }

}