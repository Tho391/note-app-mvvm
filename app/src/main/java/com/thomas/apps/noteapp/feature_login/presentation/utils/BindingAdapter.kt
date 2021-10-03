package com.thomas.apps.noteapp.feature_login.presentation.utils

import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import timber.log.Timber

@BindingAdapter("bind:username", "bind:password")
fun setEnable(view: MaterialButton, username: String?, password: String?) {
    Timber.i("setEnable $username - $password")
    val isEnable = when {
        username.isNullOrBlank() -> false
        password.isNullOrBlank() -> false
        else -> true
    }
    view.isEnabled = isEnable
}
