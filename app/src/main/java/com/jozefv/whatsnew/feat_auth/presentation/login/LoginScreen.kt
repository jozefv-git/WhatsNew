package com.jozefv.whatsnew.feat_auth.presentation.login

import WhatsNewTheme
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jozefv.whatsnew.R
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle
import com.jozefv.whatsnew.core.presentation.util.ObserveAsEvents
import com.jozefv.whatsnew.core.presentation.components.SpacerVerL
import com.jozefv.whatsnew.core.presentation.components.SpacerVerM
import com.jozefv.whatsnew.core.presentation.components.buttons.CustomActionButton
import com.jozefv.whatsnew.core.presentation.components.buttons.CustomOutlinedButton
import com.jozefv.whatsnew.core.presentation.components.CustomScaffold
import com.jozefv.whatsnew.core.presentation.components.SpacerVerS
import com.jozefv.whatsnew.core.presentation.model.ClickableIcon
import com.jozefv.whatsnew.feat_auth.presentation.components.CustomInputField
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    modifier: Modifier = Modifier,
    onSkipClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val content = LocalContext.current
    ObserveAsEvents(flow = viewModel.eventChannel) { event ->
        when (event) {
            LoginEvent.LoginSuccess -> onLoginSuccess()
            is LoginEvent.ErrorEvent -> {
                Toast.makeText(content, event.value.asString(content), Toast.LENGTH_SHORT).show()
            }
        }
    }

    LoginScreen(
        modifier = modifier,
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onSkipClick = { onSkipClick() },
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginState,
    onSkipClick: () -> Unit,
    onAction: (LoginAction) -> Unit,
) {
    val customStyle = CustomStyle.Text
    CustomScaffold(modifier = modifier) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(style = customStyle.TitleText, text = stringResource(id = R.string.whats_new))
            Text(style = customStyle.DefaultText, text = stringResource(id = R.string.introduction))
            SpacerVerL()
            CustomInputField(
                value = state.email,
                onValueChange = { onAction(LoginAction.OnEmailInput(it)) },
                keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Email),
                leadingIcon = ClickableIcon(
                    enabled = false,
                    icon = Icons.Default.Email,
                    onAction = {}),
                trailingIcon = ClickableIcon(
                    icon = Icons.Default.Clear,
                    onAction = { onAction(LoginAction.OnEraseEmail) }
                ),
                hint = stringResource(id = R.string.email_placeholder)
            )
            SpacerVerM()
            CustomInputField(
                value = state.password,
                onValueChange = { onAction(LoginAction.OnPasswordInput(it)) },
                keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Password),
                leadingIcon = ClickableIcon(
                    enabled = false,
                    icon = Icons.Default.Password,
                    onAction = {}),
                trailingIcon = ClickableIcon(
                    icon = if (state.hidePassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    onAction = { onAction(LoginAction.OnShowPassword) }
                ),
                showAsPassword = state.hidePassword,
                hint = stringResource(id = R.string.password_placeholder)
            )
            SpacerVerS()
            HorizontalDivider()
            SpacerVerL()
            CustomOutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.skip),
                onClick = {
                    onSkipClick()
                })
            SpacerVerM()
            CustomActionButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = state.canSignIn,
                text = stringResource(id = R.string.sign_in),
                onClick = {
                    onAction(LoginAction.OnSignClick)
                })
        }
    }
}

@PreviewLightDark
@Composable
private fun LoginScreenPreview() {
    WhatsNewTheme {
        LoginScreen(
            state = LoginState(),
            onSkipClick = {},
            onAction = {}
        )
    }
}
