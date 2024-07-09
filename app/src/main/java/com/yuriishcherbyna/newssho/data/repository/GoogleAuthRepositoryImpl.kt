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

    override suspend fun getGoogleToken(activityContext: Context): String? {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        Log.d(TAG, "Get Credential Request")
        return try {
            val result = credentialManager.getCredential(
                request = request,
                context = activityContext
            )
            val credential = result.credential
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            Log.d(TAG, "Get google id token credential")
            googleIdTokenCredential.idToken
        } catch (e: GetCredentialException) {
            Log.d(TAG, "GetCredentialException: ${e.message}, ${e.type}")
            null
        } catch (e: GoogleIdTokenParsingException) {
            Log.d(TAG, "Received an invalid google id token response: ${e.message}")
            null
        }
    }

    override suspend fun signIn(token: String): SignInResult {
        return try {
            val authProvider = GoogleAuthProvider.getCredential(token, null)
            Log.d(TAG, "Get authCredential")
            val user = auth.signInWithCredential(authProvider).await().user
            Log.d(TAG, "Get user: ${user?.uid}")
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
            Log.d(TAG, "Successfully logout user")
        } catch (e: Exception) {
            Log.d(TAG, "Logout error. ${e.message.toString()}")
        }
    }

    override suspend fun deleteAccount() {
        try {
            auth.currentUser?.delete()
            Log.d(TAG, "Successfully deleted account")
            logout()
            Log.d(TAG, "Successfully logout user")
        } catch (e: Exception) {
            Log.d(TAG, "Delete account error. ${e.message.toString()}")
        }
    }
}