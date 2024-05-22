package com.yuriishcherbyna.newssho.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.yuriishcherbyna.newssho.domain.repository.DataStoreRepository
import com.yuriishcherbyna.newssho.util.Constants.ONBOARDING_PREFERENCES
import com.yuriishcherbyna.newssho.util.Constants.ONBOARDING_PREFERENCES_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(ONBOARDING_PREFERENCES)

class DataStoreRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): DataStoreRepository {

    private object PreferencesKey {
        val onboardingKey = booleanPreferencesKey(ONBOARDING_PREFERENCES_KEY)
    }

    private val dataStore = context.dataStore

    override suspend fun saveOnboardingState(isCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.onboardingKey] = isCompleted
        }
    }

    override fun readOnboardingState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val onBoardingState = preferences[PreferencesKey.onboardingKey] ?: false
                onBoardingState
            }
    }
}