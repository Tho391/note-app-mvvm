package com.thomas.apps.noteapp.feature_login.presentation.login

sealed class LoginEvent{
    data class Login(val username: String, val password: String): LoginEvent()
}