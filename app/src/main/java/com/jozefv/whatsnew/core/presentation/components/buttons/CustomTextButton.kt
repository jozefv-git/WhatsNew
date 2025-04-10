package com.jozefv.whatsnew.core.presentation.components.buttons

import WhatsNewTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle


@Composable
fun CustomTextButton(
    modifier: Modifier = Modifier,
    colors: ButtonColors? = null,
    text: String,
    onClick: () -> Unit
) {
    val textStyle = if (colors == null) {
        CustomStyle.Text.ButtonText.copy(color = MaterialTheme.colorScheme.primary)
    } else {
        CustomStyle.Text.ButtonText
    }
    TextButton(
        modifier = modifier,
        colors = colors ?: ButtonDefaults.textButtonColors(),
        onClick = {
            onClick()
        }) {
        Text(
            style = textStyle,
            text = text
        )
    }
}

@Composable
fun CustomDeleteTextButton(
    modifier: Modifier = Modifier,
    colors: ButtonColors? = null,
    text: String,
    onClick: () -> Unit
) {
    val textStyle = if (colors == null) {
        CustomStyle.Text.ButtonText.copy(color = MaterialTheme.colorScheme.error)
    } else {
        CustomStyle.Text.ButtonText
    }
    TextButton(
        modifier = modifier,
        colors = colors ?: ButtonDefaults.textButtonColors(),
        onClick = {
            onClick()
        }) {
        Text(
            style = textStyle,
            text = text
        )
    }
}

@PreviewLightDark
@Composable
private fun CustomTextButtonPreview() {
    WhatsNewTheme {
        Column {
            CustomTextButton(text = "Text button", onClick = {})
            CustomDeleteTextButton(text = "Delete text button", onClick = {})
            CustomTextButton(
                colors = ButtonDefaults.textButtonColors()
                    .copy(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                text = "Text button",
                onClick = {})
            CustomDeleteTextButton(colors = ButtonDefaults.textButtonColors()
                .copy(contentColor = MaterialTheme.colorScheme.secondary),
                text = "Delete text button",
                onClick = {})
        }
    }
}