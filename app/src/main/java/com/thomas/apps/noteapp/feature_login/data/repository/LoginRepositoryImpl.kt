package com.thomas.apps.noteapp.feature_login.data.repository

import com.thomas.apps.noteapp.feature_login.data.data_source.LocalCache
import com.thomas.apps.noteapp.feature_login.data.data_source.LoginApi
import com.thomas.apps.noteapp.feature_login.data.utils.Resource
import com.thomas.apps.noteapp.feature_login.domain.model.User
import com.thomas.apps.noteapp.feature_login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow

class LoginRepositoryImpl(
    private val loginApi: LoginApi,
    private val localCache: LocalCache,
) : LoginRepository {
    override suspend fun login(username: String, password: String): Flow<Resource<User>> {
        return loginApi.login(username, password)
    }

    override suspend fun clearLocalCache() {
        localCache.clearDataStore()
    }

    override suspend fun saveUser(user: User) {
        localCache.saveToken(user.token)
        localCache.saveUserLogin(user.username)
        localCache.saveUsername(user.name)
    }
}