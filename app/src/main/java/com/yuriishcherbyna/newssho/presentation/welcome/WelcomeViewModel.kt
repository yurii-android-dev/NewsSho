package com.yuriishcherbyna.newssho.presentation.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriishcherbyna.newssho.domain.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    fun saveOnboardingState(isCompleted: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.saveOnboardingState(isCompleted)
        }
    }

}