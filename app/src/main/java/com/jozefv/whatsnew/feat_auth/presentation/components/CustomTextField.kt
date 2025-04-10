package com.jozefv.whatsnew.feat_auth.presentation.components

import WhatsNewTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jozefv.whatsnew.core.presentation.components.SpacerHorM
import com.jozefv.whatsnew.core.presentation.components.SpacerVerS
import com.jozefv.whatsnew.core.presentation.model.ClickableIcon
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.horizontalPaddingS
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.lessImportant
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.lessProminent

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    labelText: String? = null,
    placeHolder: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    showAsPassword: Boolean = false,
    singleLine: Boolean = true,
    onTrailingIconClick: () -> Unit = {},
    value: String,
    onValueChange: (String) -> Unit
) {
    val customStyle = CustomStyle.Text
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
        visualTransformation = if (showAsPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        shape = MaterialTheme.shapes.small,
        colors = TextFieldDefaults.colors().copy(
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            focusedSupportingTextColor = MaterialTheme.colorScheme.error,
            unfocusedContainerColor = Color.Transparent,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.lessImportant(),
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
            errorContainerColor = Color.Transparent,
            errorTextColor = MaterialTheme.colorScheme.onErrorContainer,
            errorLeadingIconColor = MaterialTheme.colorScheme.error,
            errorTrailingIconColor = MaterialTheme.colorScheme.error,
            errorSupportingTextColor = MaterialTheme.colorScheme.error,
        ),
        isError = isError,
        value = value,
        singleLine = singleLine,
        leadingIcon = {
            leadingIcon?.let {
                Icon(imageVector = it, contentDescription = it.name)
            }
        },
        label = {
            labelText?.let {
                Text(style = customStyle.InputFieldText, text = it)
            }
        },
        supportingText = {
            supportingText?.let {
                if (isError) {
                    Text(style = customStyle.InfoTextLessImportant, text = it)
                }
            }
        },
        placeholder = {
            placeHolder?.let {
                Text(style = customStyle.InputFieldText, text = it)
            }
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                trailingIcon?.let {
                    IconButton(
                        onClick = {
                            onTrailingIconClick()
                        }
                    ) {
                        Icon(
                            imageVector = it,
                            contentDescription = it.name
                        )
                    }
                }
            }
        },
        onValueChange = {
            onValueChange(it)
        }
    )

}

@Composable
fun CustomInputField(
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
    keyboardActions: KeyboardActions? = null,
    leadingIcon: ClickableIcon? = null,
    trailingIcon: ClickableIcon? = null,
    trailingIsLoading: Boolean = false,
    hint: String? = null,
    supportingText: String? = null,
    isSupportingTextError: Boolean = false,
    showAsPassword: Boolean = false,
    singleLine: Boolean = true,
    value: String,
    onValueChange: (String) -> Unit
) {

    var inputFocused by remember {
        mutableStateOf(false)
    }
    val errorColor = if (inputFocused) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.error.lessProminent()
    }
    val color =
        if (inputFocused) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.primary.lessProminent()

        }
    val supportingTextVisibility = if (supportingText != null) 1f else 0f
    Column(modifier = modifier.background(MaterialTheme.colorScheme.surface)) {
        Row(
            Modifier
                .border(
                    width = 2.dp,
                    color = if (!isSupportingTextError) color else errorColor,
                    shape = MaterialTheme.shapes.small
                )
                .horizontalPaddingS(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon?.let {
                IconButton(
                    enabled = it.enabled,
                    onClick = { it.onAction() }) {
                    Icon(
                        tint = if (!isSupportingTextError) color else errorColor,
                        imageVector = it.icon,
                        contentDescription = it.icon.name
                    )
                }
            }
            SpacerHorM()
            BasicTextField(
                modifier = Modifier
                    .onFocusChanged {
                        inputFocused = it.hasFocus
                    }
                    .weight(1f)
                    .defaultMinSize(minHeight = 56.dp),
                visualTransformation = if (showAsPassword) PasswordVisualTransformation() else VisualTransformation.None,
                textStyle = CustomStyle.Text.InputFieldText.copy(
                    color = if (!isSupportingTextError) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
                ),
                cursorBrush = if (!isSupportingTextError) SolidColor(MaterialTheme.colorScheme.primary) else SolidColor(
                    MaterialTheme.colorScheme.error
                ),
                singleLine = singleLine,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions ?: KeyboardActions.Default,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty() && hint != null) {
                            Text(
                                style = CustomStyle.Text.InfoText.copy(color = MaterialTheme.colorScheme.onSurface.lessImportant()),
                                text = hint
                            )
                        }
                        // Actual text field
                        innerTextField()
                    }
                },
                value = value,
                onValueChange = { onValueChange(it) }
            )
            SpacerHorM()
            trailingIcon?.let {
                if (trailingIsLoading) {
                    CircularProgressIndicator(color = if (!isSupportingTextError) color else errorColor)
                } else {
                    IconButton(
                        enabled = it.enabled,
                        onClick = { it.onAction() }) {
                        Icon(
                            tint = if (!isSupportingTextError) color else errorColor,
                            imageVector = it.icon,
                            contentDescription = it.icon.name
                        )
                    }
                }
            }
        }
        Column(Modifier.alpha(supportingTextVisibility)) {
            SpacerVerS()
            Text(
                modifier = Modifier,
                style = CustomStyle.Text.InfoTextLessImportant.copy(
                    color = if (!isSupportingTextError)
                        MaterialTheme.colorScheme.onSurface.lessImportant() else errorColor
                ),
                text = supportingText ?: ""
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun CustomTextFieldPreview() {
    WhatsNewTheme {
        Column {
            CustomTextField(
                labelText = "Name",
                value = "",
                supportingText = "Name",
                trailingIcon = Icons.Default.Clear,
                onTrailingIconClick = {},
                onValueChange = {}
            )
            CustomTextField(
                labelText = "Email",
                value = "Email",
                supportingText = "Email",
                leadingIcon = Icons.Default.Email,
                trailingIcon = Icons.Default.Clear,
                onTrailingIconClick = {},
                onValueChange = {}
            )
            CustomTextField(
                labelText = "Password",
                value = "Error",
                isError = true,
                supportingText = "This is error",
                trailingIcon = Icons.Default.Clear,
                onTrailingIconClick = {},
                onValueChange = {}
            )
            CustomInputField(
                value = "",
                hint = "Email",
                supportingText = "Name",
                leadingIcon = ClickableIcon(icon = Icons.Default.Email, onAction = {}),
                trailingIcon = ClickableIcon(icon = Icons.Default.Clear, onAction = {}),
                onValueChange = {}
            )
            CustomInputField(
                value = "Value",
                supportingText = "Name",
                isSupportingTextError = true,
                leadingIcon = ClickableIcon(icon = Icons.Default.Email, onAction = {}),
                trailingIcon = ClickableIcon(icon = Icons.Default.Clear, onAction = {}),
                onValueChange = {}
            )
            CustomInputField(
                trailingIsLoading = true,
                value = "Value",
                supportingText = "Name",
                leadingIcon = ClickableIcon(icon = Icons.Default.Email, onAction = {}),
                trailingIcon = ClickableIcon(icon = Icons.Default.Clear, onAction = {}),
                onValueChange = {}
            )
        }
    }
}