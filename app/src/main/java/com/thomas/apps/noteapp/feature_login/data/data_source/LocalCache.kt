package com.thomas.apps.noteapp.feature_login.data.data_source

import android.app.Application
import com.thomas.apps.noteapp.feature_login.presentation.utils.DataStore.clearDataStore
import com.thomas.apps.noteapp.feature_login.presentation.utils.DataStore.getToken
import com.thomas.apps.noteapp.feature_login.presentation.utils.DataStore.saveToken
import com.thomas.apps.noteapp.feature_login.presentation.utils.DataStore.saveUserLogin
import com.thomas.apps.noteapp.feature_login.presentation.utils.DataStore.saveUsername
import kotlinx.coroutines.flow.Flow

class LocalCache(application: Application) {
    private val context = application.applicationContext

    suspend fun saveToken(token: String?) {
        context.saveToken(token)
    }

    fun getToken(): Flow<String> {
        return context.getToken()
    }

    suspend fun saveUsername(name: String?) {
        context.saveUsername(name)
    }

    suspend fun saveUserLogin(login: String?) {
        context.saveUserLogin(login)
    }

    suspend fun clearDataStore() {
        context.clearDataStore()
    }
}