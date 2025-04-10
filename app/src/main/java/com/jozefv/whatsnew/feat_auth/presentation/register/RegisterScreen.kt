package com.jozefv.whatsnew.feat_auth.presentation.register

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.jozefv.whatsnew.core.presentation.components.InfoIconRowText
import com.jozefv.whatsnew.core.presentation.model.ClickableIcon
import com.jozefv.whatsnew.feat_auth.presentation.components.CustomInputField
import org.koin.androidx.compose.koinViewModel


@Composable
fun RegisterScreenRoot(
    modifier: Modifier = Modifier,
    onSuccessfulRegistration: () -> Unit, // Propagate navigation to navGraph
    onSkipClick: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.eventChannel) { event ->
        when (event) {
            RegisterEvent.OnRegistrationSuccess -> onSuccessfulRegistration()
            is RegisterEvent.ErrorEvent -> {
                Toast.makeText(context, event.value.asString(context), Toast.LENGTH_SHORT).show()
            }
        }

    }
    RegisterScreen(
        modifier = modifier,
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onSkipClick = { onSkipClick() },
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}


@Composable
private fun RegisterScreen(
    modifier: Modifier = Modifier,
    state: RegisterState,
    onSkipClick: () -> Unit,
    onAction: (RegisterAction) -> Unit
) {
    val customStyle = CustomStyle.Text
    CustomScaffold(modifier = modifier) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    style = customStyle.HeadlineText,
                    text = stringResource(id = R.string.create_account)
                )
                SpacerVerL()
                CustomInputField(
                    value = state.nick,
                    onValueChange = { onAction(RegisterAction.OnNickInput(it)) },
                    leadingIcon = ClickableIcon(
                        enabled = false,
                        icon = Icons.Default.Person,
                        onAction = {}),
                    trailingIcon = ClickableIcon(
                        icon = Icons.Default.Clear,
                        onAction = { onAction(RegisterAction.OnEraseNick) }),
                    hint = stringResource(id = R.string.nick_placeholder),
                    isSupportingTextError = state.nickError != null,
                    supportingText = state.nickError?.asComposeString()
                )
                SpacerVerM()
                CustomInputField(
                    value = state.email,
                    onValueChange = { onAction(RegisterAction.OnEmailInput(it)) },
                    keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Email),
                    leadingIcon = ClickableIcon(
                        enabled = false,
                        icon = Icons.Default.Email,
                        onAction = {}),
                    trailingIcon = ClickableIcon(
                        icon = Icons.Default.Clear,
                        onAction = { onAction(RegisterAction.OnEraseEmail) }
                    ),
                    hint = stringResource(id = R.string.email_placeholder),
                    isSupportingTextError = state.emailError != null,
                    supportingText = state.emailError?.asComposeString()
                )
                SpacerVerM()
                CustomInputField(
                    value = state.password,
                    onValueChange = { onAction(RegisterAction.OnPasswordInput(it)) },
                    keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Password),
                    leadingIcon = ClickableIcon(
                        enabled = false,
                        icon = Icons.Default.Password,
                        onAction = {}),
                    trailingIcon = ClickableIcon(
                        icon = if (state.hidePassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        onAction = { onAction(RegisterAction.OnShowPassword) }
                    ),
                    showAsPassword = state.hidePassword,
                    hint = stringResource(id = R.string.password_placeholder),
                    isSupportingTextError = state.passwordError != null,
                    supportingText = state.passwordError?.asComposeString()
                )
                SpacerVerL()
            }
            Column {
                InfoIconRowText(text = stringResource(id = R.string.only_one_account_per_device))
                SpacerVerM()
                InfoIconRowText(text = stringResource(id = R.string.after_successfull_registration))
                SpacerVerM()
                InfoIconRowText(text = stringResource(id = R.string.if_you_forget_credentials))
                SpacerVerL()
                CustomOutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.skip),
                    onClick = { onSkipClick() })
                SpacerVerM()
                CustomActionButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.canRegister,
                    text = stringResource(id = R.string.sign_up),
                    onClick = {
                        onAction(RegisterAction.OnRegisterClick)
                    })
            }
        }

    }
}


@PreviewLightDark
@Composable
private fun RegisterScreenPreview() {
    WhatsNewTheme {
        RegisterScreen(
            state = RegisterState(),
            onSkipClick = {},
            onAction = {}
        )
    }
}