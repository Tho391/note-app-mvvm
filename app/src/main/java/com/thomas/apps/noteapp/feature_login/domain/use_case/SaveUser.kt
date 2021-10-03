package com.thomas.apps.noteapp.feature_login.domain.use_case

import android.content.Context
import com.thomas.apps.noteapp.feature_login.domain.model.User
import com.thomas.apps.noteapp.feature_login.domain.repository.LoginRepository
import com.thomas.apps.noteapp.feature_login.presentation.utils.DataStore.saveToken
import com.thomas.apps.noteapp.feature_login.presentation.utils.DataStore.saveUserLogin
import com.thomas.apps.noteapp.feature_login.presentation.utils.DataStore.saveUsername

data class SaveUser(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(user: User) {
        loginRepository.saveUser(user)
    }
}
