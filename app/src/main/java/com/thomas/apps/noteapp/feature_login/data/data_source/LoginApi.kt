package com.thomas.apps.noteapp.feature_login.data.data_source

import com.thomas.apps.noteapp.feature_login.data.utils.Resource
import com.thomas.apps.noteapp.feature_login.domain.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginApi {
    suspend fun login(
        username: String,
        password: String
    ): Flow<Resource<User>> {
        return flow {
            emit(Resource.Loading())

            delay(3000)

            if (username == "tho" && password == "123456") {
                val success = Resource.Success<User>(
                    User(
                        name = "Thọ",
                        username = "tho",
                        token = "this_is_token"
                    )
                )
                emit(success)
            } else {
                emit(Resource.Error("Invalid credentials"))
            }
        }
    }
}