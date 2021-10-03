package com.thomas.apps.noteapp.feature_login.domain.model

data class User(
    val name: String,
    val username: String,
    val token: String?
)
