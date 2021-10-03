package com.thomas.apps.noteapp.feature_login.domain.use_case

data class LoginUseCases(
    val login: Login,
    val saveUser: SaveUser,
    val logout: Logout
)