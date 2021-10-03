package com.thomas.apps.noteapp.feature_login.domain.use_case

import com.thomas.apps.noteapp.feature_login.data.utils.Resource
import com.thomas.apps.noteapp.feature_login.domain.model.User
import com.thomas.apps.noteapp.feature_login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow

data class Login(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(username: String, password: String): Flow<Resource<User>> {
        return repository.login(username, password)
    }
}
