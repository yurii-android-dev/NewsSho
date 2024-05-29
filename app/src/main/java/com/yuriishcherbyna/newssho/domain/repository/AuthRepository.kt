package com.yuriishcherbyna.newssho.domain.repository

import com.yuriishcherbyna.newssho.domain.model.SignInResult
import com.yuriishcherbyna.newssho.domain.model.UserData

interface AuthRepository {

    suspend fun getGoogleToken(): String?

    suspend fun signIn(token: String): SignInResult

    fun getUserData(): UserData?

    suspend fun logout()

    suspend fun deleteAccount()

}