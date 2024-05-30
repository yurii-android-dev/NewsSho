package com.yuriishcherbyna.newssho.data.repository

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.yuriishcherbyna.newssho.R
import com.yuriishcherbyna.newssho.domain.model.SignInResult
import com.yuriishcherbyna.newssho.domain.model.UserData
import com.yuriishcherbyna.newssho.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "GoogleAuthRepositoryImpl"

class GoogleAuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager
): AuthRepository {

    override suspend fun getGoogleToken(): String? {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            val credential = result.credential
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            googleIdTokenCredential.idToken
        } catch (e: GetCredentialException) {
            Log.d(TAG, e.message.toString())
            null
        } catch (e: GoogleIdTokenParsingException) {
            Log.d(TAG, e.message.toString())
            null
        }
    }

    override suspend fun signIn(token: String): SignInResult {
        return try {
            val authProvider = GoogleAuthProvider.getCredential(token, null)
            val user = auth.signInWithCredential(authProvider).await().user
            SignInResult(
                userData = user?.run {
                    UserData(
                        id = uid,
                        imageUrl = photoUrl?.toString(),
                        name = displayName
                    )
                },
                error = null
            )
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            SignInResult(userData = null, error = null)
        }
    }

    override fun getUserData(): UserData? {
        return auth.currentUser?.run {
            UserData(
                id = uid,
                name = displayName,
                imageUrl = photoUrl?.toString()
            )
        }
    }

    override suspend fun logout() {
        try {
            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
    }

    override suspend fun deleteAccount() {
        try {
            auth.currentUser?.delete()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
    }
}