package com.jozefv.whatsnew.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jozefv.whatsnew.core.domain.SessionStorage
import kotlinx.coroutines.launch

class MainViewModel(private val sessionStorage: SessionStorage) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(isAuthenticating = true)
            val user = sessionStorage.getUser()
            state = if (user == null) {
                state.copy(isFirstSession = true, isAuthenticating = false)
            } else {
                state.copy(isLoggedIn = user.isLoggedIn)
            }
            state = state.copy(isAuthenticating = false)
        }
    }
}