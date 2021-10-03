package com.thomas.apps.noteapp.feature_login.presentation.login

data class LoginTextFieldState(
    var text: String = "",
) {
    val error: String?
        get() = if (text.isBlank()) {
            "Required"
        } else {
            null
        }
}
