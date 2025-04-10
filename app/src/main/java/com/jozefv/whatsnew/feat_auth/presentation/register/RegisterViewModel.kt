package com.jozefv.whatsnew.feat_auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jozefv.whatsnew.R
import com.jozefv.whatsnew.core.domain.util.CoroutineDispatchers
import com.jozefv.whatsnew.core.domain.util.ResultHandler
import com.jozefv.whatsnew.core.presentation.util.UiText
import com.jozefv.whatsnew.core.presentation.util.toUiText
import com.jozefv.whatsnew.feat_auth.domain.AuthRepository
import com.jozefv.whatsnew.feat_auth.domain.InputUserDataValidator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class RegisterViewModel(
    private val inputUserDataValidator: InputUserDataValidator,
    private val authRepository: AuthRepository,
    private val coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {


    private var _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<RegisterEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    init {
        state.onEach {
            _state.update { s ->
                s.copy(
                    canRegister = it.nickError == null
                            && it.emailError == null
                            && it.passwordError == null
                            && it.nick.isNotEmpty()
                            && it.email.isNotEmpty()
                            && it.password.isNotEmpty()
                )
            }
        }.launchIn(viewModelScope.plus(coroutineDispatchers.mainImmediate))
    }

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnNickInput -> {
                val nick = action.value
                val error = if (inputUserDataValidator.isNickValid(nick)) {
                    null
                } else {
                    UiText.StringResource(R.string.nick_invalid)
                }
                _state.update { it.copy(nick = nick, nickError = error) }
                if (state.value.nick.isEmpty()) {
                    // Reset possible error if user delete input
                    _state.update { it.copy(nickError = null) }
                }
            }

            RegisterAction.OnEraseNick -> {
                _state.update { it.copy(nick = "", nickError = null) }
            }

            is RegisterAction.OnEmailInput -> {
                val email = action.value
                val error =
                    if (inputUserDataValidator.isEmailValid(email)) {
                        null
                    } else {
                        UiText.StringResource(R.string.email_invalid)
                    }
                _state.update { it.copy(email = email, emailError = error) }
                if (state.value.email.isEmpty()) {
                    _state.update { it.copy(emailError = null) }
                }
            }

            RegisterAction.OnEraseEmail -> {
                _state.update { it.copy(email = "", emailError = null) }
            }

            is RegisterAction.OnPasswordInput -> {
                val password = action.value
                val error =
                    if (inputUserDataValidator.isPasswordValid(password)) {
                        null
                    } else {
                        UiText.StringResource(R.string.password_invalid)
                    }
                _state.update { it.copy(password = password, passwordError = error) }

                if (state.value.password.isEmpty()) {
                    _state.update { it.copy(passwordError = null) }
                }
            }


            RegisterAction.OnShowPassword -> {
                _state.update { it.copy(hidePassword = !state.value.hidePassword) }
            }

            RegisterAction.OnRegisterClick -> {
                viewModelScope.launch(coroutineDispatchers.mainImmediate) {
                    val result = authRepository.register(
                        nick = state.value.nick,
                        email = state.value.email,
                        password = state.value.password
                    )
                    when (result) {
                        is ResultHandler.Error -> {
                            _eventChannel.send(RegisterEvent.ErrorEvent(value = result.toUiText()))
                        }

                        is ResultHandler.Success -> {
                            _eventChannel.send(RegisterEvent.OnRegistrationSuccess)
                        }
                    }
                }
            }
        }
    }
}