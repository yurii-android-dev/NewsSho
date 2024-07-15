package com.yuriishcherbyna.newssho.presentation.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriishcherbyna.newssho.domain.model.UserData
import com.yuriishcherbyna.newssho.domain.repository.AuthRepository
import com.yuriishcherbyna.newssho.domain.repository.BookmarksNewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val bookmarksNewsRepository: BookmarksNewsRepository
): ViewModel(){

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData = _userData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    var showDialog by mutableStateOf(false)
        private set

    var isUserDeleted by mutableStateOf(false)
        private set

    init {
        getUserData()
    }

    private fun getUserData() {
        _isLoading.value = true
        authRepository.getUserData().apply {
            _userData.value = this
            _isLoading.value = false
        }
    }

    fun logOut() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            bookmarksNewsRepository.deleteAllNews(20)
            authRepository.deleteAccount()
            authRepository.logout()
            isUserDeleted = true
        }
    }

    fun changeShowDialogValue(value: Boolean) {
        showDialog = value
    }

}