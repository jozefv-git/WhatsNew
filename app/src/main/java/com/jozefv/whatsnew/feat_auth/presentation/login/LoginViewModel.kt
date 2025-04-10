package com.jozefv.whatsnew.feat_auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jozefv.whatsnew.core.domain.util.ResultHandler
import com.jozefv.whatsnew.core.presentation.util.toUiText
import com.jozefv.whatsnew.feat_auth.domain.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private var _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<LoginEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    init {
        state.onEach {
            _state.update { s ->
                s.copy(
                    canSignIn = it.email.isNotEmpty() && it.password.isNotEmpty()
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailInput -> {
                _state.update { it.copy(email = action.email) }
            }

            LoginAction.OnEraseEmail -> {
                _state.update { it.copy(email = "") }
            }

            is LoginAction.OnPasswordInput -> {
                _state.update { it.copy(password = action.password) }
            }

            LoginAction.OnShowPassword -> {
                _state.update { it.copy(hidePassword = !state.value.hidePassword) }
            }

            LoginAction.OnSignClick -> {
                viewModelScope.launch {
                    val result = authRepository.login(
                        email = state.value.email,
                        password = state.value.password
                    )
                    when (result) {
                        is ResultHandler.Success -> {
                            _eventChannel.send(LoginEvent.LoginSuccess)
                        }

                        is ResultHandler.Error -> {
                            _eventChannel.send(LoginEvent.ErrorEvent(result.toUiText()))
                        }
                    }
                }
            }
        }
    }

}