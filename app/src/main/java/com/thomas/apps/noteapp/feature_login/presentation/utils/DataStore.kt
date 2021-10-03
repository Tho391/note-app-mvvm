package com.thomas.apps.noteapp.feature_login.presentation.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStore {
    private val TOKEN_KEY = stringPreferencesKey("TOKEN_KEY")
    private val USER_NAME = stringPreferencesKey("USER_NAME")
    private val USER_LOGIN = stringPreferencesKey("USER_LOGIN")

    val Context.dataStore by preferencesDataStore(name = "settings")

    suspend fun Context.saveToken(token: String?) {
        dataStore.edit { setting ->
            setting[TOKEN_KEY] = token ?: ""
        }
    }

    fun Context.getToken(): Flow<String> {
        return dataStore.data.map { preferences -> preferences[TOKEN_KEY] ?: "" }
    }

    suspend fun Context.saveUsername(name: String?) {
        dataStore.edit { setting ->
            setting[USER_NAME] = name ?: ""
        }
    }

    suspend fun Context.saveUserLogin(login: String?) {
        dataStore.edit { setting ->
            setting[USER_LOGIN] = login ?: ""
        }
    }

    suspend fun Context.clearDataStore() {
        dataStore.edit { it.clear() }
    }
}