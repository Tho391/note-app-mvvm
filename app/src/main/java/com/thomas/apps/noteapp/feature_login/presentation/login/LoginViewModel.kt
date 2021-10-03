package com.thomas.apps.noteapp.feature_login.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thomas.apps.noteapp.feature_login.data.utils.Resource
import com.thomas.apps.noteapp.feature_login.domain.use_case.LoginUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases
) : ViewModel() {

    private val _username = MutableStateFlow(LoginTextFieldState())
    val username = _username.asStateFlow()

    private val _password = MutableStateFlow(LoginTextFieldState())
    val password = _password.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.Login -> {
                viewModelScope.launch {
                    loginUseCases.login(event.username, event.password).collect { resource ->
                        when (resource) {
                            is Resource.Loading -> {
                                _eventFlow.emit(UIEvent.Loading(true))
                            }
                            is Resource.Success -> {
                                _eventFlow.emit(UIEvent.Loading(false))
                                _eventFlow.emit(UIEvent.LoginSuccess)
                            }
                            is Resource.Error -> {
                                _eventFlow.emit(UIEvent.Loading(false))
                                _eventFlow.emit(UIEvent.ShowSnackbar(resource.msg))
                            }
                        }
                    }
                }
            }
        }
    }

    sealed class UIEvent {
        data class Loading(val isLoading: Boolean) : UIEvent()
        data class ShowSnackbar(val message: String) : UIEvent()
        object LoginSuccess : UIEvent()
    }
}