package com.yuriishcherbyna.newssho.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun saveOnboardingState(isCompleted: Boolean)

    fun readOnboardingState(): Flow<Boolean>

}