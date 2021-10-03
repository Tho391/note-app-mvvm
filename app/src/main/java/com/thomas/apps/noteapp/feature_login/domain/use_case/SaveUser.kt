package com.thomas.apps.noteapp.feature_login.domain.use_case

import android.content.Context
import com.thomas.apps.noteapp.feature_login.domain.model.User
import com.thomas.apps.noteapp.feature_login.presentation.utils.DataStore.saveToken
import com.thomas.apps.noteapp.feature_login.presentation.utils.DataStore.saveUserLogin
import com.thomas.apps.noteapp.feature_login.presentation.utils.DataStore.saveUsername

data class SaveUser(
    private val context: Context
) {
    suspend operator fun invoke(user: User) {
        context.saveToken(user.token)
        context.saveUsername(user.name)
        context.saveUserLogin(user.username)
    }
}
