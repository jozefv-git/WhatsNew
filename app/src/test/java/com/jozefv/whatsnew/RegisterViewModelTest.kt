@file:OptIn(ExperimentalCoroutinesApi::class)

package com.jozefv.whatsnew

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.jozefv.whatsnew.core.domain.SessionStorageFake
import com.jozefv.whatsnew.core.domain.TestCoroutineDispatchers
import com.jozefv.whatsnew.feat_auth.data.PrefsAuthRepository
import com.jozefv.whatsnew.feat_auth.domain.EmailPatternValidator
import com.jozefv.whatsnew.feat_auth.domain.InputUserDataValidator
import com.jozefv.whatsnew.feat_auth.presentation.register.RegisterAction
import com.jozefv.whatsnew.feat_auth.presentation.register.RegisterEvent
import com.jozefv.whatsnew.feat_auth.presentation.register.RegisterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

// Integration test - we want to test multiple units together - so we don't replace
// repository or InputUserDataValidator - as we want to test their logic
// We replace only boundaries - so where our data leaves the device - prefsAuthRepository doesn't
// communicate directly with any external services - session storage is our boundary as that uses
// encryptedSharedPrefs - which saves into the file - so boundary - our data leaves the app...
class RegisterViewModelTest {
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var authRepository: PrefsAuthRepository
    private lateinit var sessionStorageFake: SessionStorageFake
    private lateinit var inputUserDataValidator: InputUserDataValidator
    private lateinit var testDispatcher: TestCoroutineDispatchers

    private val nickInput = "Jozef"
    private val emailInput = "email@example.com"
    private val passwordInput = "password122"

    @BeforeEach
    fun setUp() {
        // Stub pattern validator

        val emailPatternValidator = object : EmailPatternValidator {
            override fun isEmailValid(email: String): Boolean {
                return true
            }
        }
        testDispatcher = TestCoroutineDispatchers(StandardTestDispatcher())
        inputUserDataValidator = InputUserDataValidator(emailPatternValidator)
        sessionStorageFake = SessionStorageFake()
        authRepository = PrefsAuthRepository(sessionStorageFake)
        // We can construct VM like this - as there is no lifecycle involved - as we don't run end-to-end UI testing
        // where we would need to construct VM with lifecycle - so with VM factory
        registerViewModel = RegisterViewModel(
            inputUserDataValidator = inputUserDataValidator,
            authRepository = authRepository,
            coroutineDispatchers = testDispatcher
        )
    }

    @Test
    fun canRegister() = runTest(testDispatcher.testDispatcher) {
        assertThat(registerViewModel.state.value.nick).isEmpty()
        assertThat(registerViewModel.state.value.nickError).isNull()
        assertThat(registerViewModel.state.value.email).isEmpty()
        assertThat(registerViewModel.state.value.emailError).isNull()
        assertThat(registerViewModel.state.value.password).isEmpty()
        assertThat(registerViewModel.state.value.passwordError).isNull()
        assertThat(registerViewModel.state.value.canRegister).isFalse()
        registerViewModel.onAction(RegisterAction.OnNickInput(nickInput))
        registerViewModel.onAction(RegisterAction.OnEmailInput(emailInput))
        registerViewModel.onAction(RegisterAction.OnPasswordInput(passwordInput))
        // After we triggered the flow with actions - listen to it
        runCurrent()
        assertThat(registerViewModel.state.value.canRegister).isTrue()
    }

    @Test
    fun erasedNick() {
        assertThat(registerViewModel.state.value.nick).isEmpty()
        registerViewModel.onAction(RegisterAction.OnNickInput(nickInput))
        assertThat(registerViewModel.state.value.nick).isEqualTo(nickInput)
        registerViewModel.onAction(RegisterAction.OnEraseNick)
        assertThat(registerViewModel.state.value.nick).isEmpty()
    }

    @Test
    fun erasedEmail() {
        assertThat(registerViewModel.state.value.email).isEmpty()
        registerViewModel.onAction(RegisterAction.OnEmailInput(emailInput))
        assertThat(registerViewModel.state.value.email).isEqualTo(emailInput)
        registerViewModel.onAction(RegisterAction.OnEraseEmail)
        assertThat(registerViewModel.state.value.email).isEmpty()
    }

    @Test
    fun showPassword() {
        assertThat(registerViewModel.state.value.password).isEmpty()
        assertThat(registerViewModel.state.value.hidePassword).isTrue()
        registerViewModel.onAction(RegisterAction.OnPasswordInput(passwordInput))
        assertThat(registerViewModel.state.value.password).isEqualTo(passwordInput)
        registerViewModel.onAction(RegisterAction.OnShowPassword)
        assertThat(registerViewModel.state.value.hidePassword).isFalse()
    }

    @Test
    fun isNickError() {
        assertThat(registerViewModel.state.value.nick).isEmpty()
        assertThat(registerViewModel.state.value.nickError).isNull()
        registerViewModel.onAction(RegisterAction.OnNickInput(" "))
        assertThat(registerViewModel.state.value.nickError).isNotNull()
    }

    @Test
    fun isPasswordError() {
        assertThat(registerViewModel.state.value.password).isEmpty()
        assertThat(registerViewModel.state.value.passwordError).isNull()
        registerViewModel.onAction(RegisterAction.OnPasswordInput("noNumbersIncluded"))
        assertThat(registerViewModel.state.value.passwordError).isNotNull()
    }

    @Test
    fun clearNickError() {
        assertThat(registerViewModel.state.value.nickError).isNull()
        registerViewModel.onAction(RegisterAction.OnNickInput(" "))
        assertThat(registerViewModel.state.value.nickError).isNotNull()
        registerViewModel.onAction(RegisterAction.OnEraseNick)
        assertThat(registerViewModel.state.value.nickError).isNull()
    }


    @Test
    fun didRegister() = runTest(testDispatcher.testDispatcher) {
        assertThat(registerViewModel.state.value.nick).isEmpty()
        assertThat(registerViewModel.state.value.nickError).isNull()
        assertThat(registerViewModel.state.value.email).isEmpty()
        assertThat(registerViewModel.state.value.emailError).isNull()
        assertThat(registerViewModel.state.value.password).isEmpty()
        assertThat(registerViewModel.state.value.passwordError).isNull()
        assertThat(registerViewModel.state.value.canRegister).isFalse()

        registerViewModel.onAction(RegisterAction.OnNickInput(nickInput))
        registerViewModel.onAction(RegisterAction.OnEmailInput(emailInput))
        registerViewModel.onAction(RegisterAction.OnPasswordInput(passwordInput))
        runCurrent() // For listening the state changes
        assertThat(registerViewModel.state.value.nick).isEqualTo(nickInput)
        assertThat(registerViewModel.state.value.nickError).isNull()
        assertThat(registerViewModel.state.value.email).isEqualTo(emailInput)
        assertThat(registerViewModel.state.value.emailError).isNull()
        assertThat(registerViewModel.state.value.password).isEqualTo(passwordInput)
        assertThat(registerViewModel.state.value.passwordError).isNull()
        assertThat(registerViewModel.state.value.canRegister).isTrue()

        registerViewModel.onAction(RegisterAction.OnRegisterClick)
        runCurrent() // for jump into the coroutine
        val session = sessionStorageFake.getUser()
        assertThat(session).isNotNull()
        assertThat(session?.nick).isEqualTo(nickInput)
        assertThat(session?.email).isEqualTo(emailInput)
        assertThat(session?.password).isEqualTo(passwordInput)
        assertThat(session?.isLoggedIn).isEqualTo(true)

        registerViewModel.eventChannel.test {
            assertThat(expectMostRecentItem()).isEqualTo(RegisterEvent.OnRegistrationSuccess)
            cancelAndIgnoreRemainingEvents()
        }
    }
}