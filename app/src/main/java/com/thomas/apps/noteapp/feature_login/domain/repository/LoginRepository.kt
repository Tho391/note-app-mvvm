package com.thomas.apps.noteapp.feature_login.domain.repository

import com.thomas.apps.noteapp.feature_login.data.utils.Resource
import com.thomas.apps.noteapp.feature_login.domain.model.User
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun login(username: String, password: String): Flow<Resource<User>>
}