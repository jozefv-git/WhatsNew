package com.jozefv.whatsnew.core.presentation.components.buttons

import WhatsNewTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle
import com.jozefv.whatsnew.core.presentation.components.SpacerHorS
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.horizontalPaddingS
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.lessProminent
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.verticalPaddingXS

@Composable
fun CustomActionButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.lessProminent(),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary.lessProminent()
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick() }) {
        Box(
            Modifier
                .horizontalPaddingS()
                .verticalPaddingXS(),
            contentAlignment = Alignment.Center

        ) {
            Row(
                modifier = Modifier.alpha(if (isLoading) 0f else 1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = it.name
                    )
                    SpacerHorS()
                }
                Text(style = CustomStyle.Text.ButtonText, text = text)
            }
            CircularProgressIndicator(modifier = Modifier.alpha(if (isLoading) 1f else 0f))
        }
    }
}

@Composable
fun CustomDeleteActionButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
            disabledContainerColor = MaterialTheme.colorScheme.error.lessProminent(),
            disabledContentColor = MaterialTheme.colorScheme.onError.lessProminent()
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick() }) {
        Box(
            Modifier
                .horizontalPaddingS()
                .verticalPaddingXS(),
            contentAlignment = Alignment.Center

        ) {
            Row(
                modifier = Modifier.alpha(if (isLoading) 0f else 1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = it.name
                    )
                    SpacerHorS()
                }
                Text(style = CustomStyle.Text.ButtonText, text = text)
            }
            CircularProgressIndicator(modifier = Modifier.alpha(if (isLoading) 1f else 0f))
        }
    }
}

@Composable
fun CustomOutlinedButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    text: String,
    onClick: () -> Unit
) {
    val borderColor = if (enabled) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.primary.lessProminent()
    }
    OutlinedButton(
        modifier = modifier,
        enabled = enabled,
        border = BorderStroke(1.dp, color = borderColor),
        colors = ButtonDefaults.outlinedButtonColors().copy(
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.primary.lessProminent()
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick() }) {
        Box(
            Modifier
                .horizontalPaddingS()
                .verticalPaddingXS(),
            contentAlignment = Alignment.Center

        ) {
            Row(
                modifier = Modifier.alpha(if (isLoading) 0f else 1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = it.name
                    )
                    SpacerHorS()
                }
                Text(style = CustomStyle.Text.ButtonText, text = text)
            }
            CircularProgressIndicator(modifier = Modifier.alpha(if (isLoading) 1f else 0f))
        }
    }
}

@Composable
fun CustomDeleteOutlinedButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    text: String,
    onClick: () -> Unit
) {
    val borderColor = if (enabled) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.error.lessProminent()
    }
    OutlinedButton(
        modifier = modifier,
        enabled = enabled,
        border = BorderStroke(1.dp, color = borderColor),
        colors = ButtonDefaults.outlinedButtonColors().copy(
            contentColor = MaterialTheme.colorScheme.error,
            disabledContentColor = MaterialTheme.colorScheme.error.lessProminent()
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick() }) {
        Box(
            Modifier
                .horizontalPaddingS()
                .verticalPaddingXS(),
            contentAlignment = Alignment.Center

        ) {
            Row(
                modifier = Modifier.alpha(if (isLoading) 0f else 1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = it.name
                    )
                    SpacerHorS()
                }
                Text(style = CustomStyle.Text.ButtonText, text = text)
            }
            CircularProgressIndicator(modifier = Modifier.alpha(if (isLoading) 1f else 0f))
        }
    }
}


@PreviewLightDark
@Composable
private fun CustomActionButtonPreview() {
    WhatsNewTheme {
        Column {
            Row {
                CustomActionButton(
                    modifier = Modifier.weight(1f),
                    enabled = true,
                    isLoading = false,
                    icon = Icons.Default.Clear,
                    text = "Enabled",
                    onClick = {}
                )
                CustomActionButton(
                    modifier = Modifier.weight(1f),
                    enabled = false,
                    isLoading = false,
                    icon = Icons.Default.Clear,
                    text = "Disabled",
                    onClick = {}
                )
            }
            Row {
                CustomOutlinedButton(
                    modifier = Modifier.weight(1f),
                    enabled = true,
                    isLoading = false,
                    icon = Icons.Default.Clear,
                    text = "Enabled",
                    onClick = {}
                )
                CustomOutlinedButton(
                    modifier = Modifier.weight(1f),
                    enabled = false,
                    isLoading = false,
                    icon = Icons.Default.Clear,
                    text = "Disabled",
                    onClick = {}
                )
            }
            Row {
                CustomDeleteActionButton(
                    modifier = Modifier.weight(1f),
                    enabled = true,
                    isLoading = false,
                    icon = Icons.Default.Clear,
                    text = "Enabled",
                    onClick = {}
                )
                CustomDeleteActionButton(
                    modifier = Modifier.weight(1f),
                    enabled = false,
                    isLoading = false,
                    icon = Icons.Default.Clear,
                    text = "Disabled",
                    onClick = {}
                )
            }
            Row {
                CustomDeleteOutlinedButton(
                    modifier = Modifier.weight(1f),
                    enabled = true,
                    isLoading = false,
                    icon = Icons.Default.Clear,
                    text = "Enabled",
                    onClick = {}
                )
                CustomDeleteOutlinedButton(
                    modifier = Modifier.weight(1f),
                    enabled = false,
                    isLoading = false,
                    icon = Icons.Default.Clear,
                    text = "Disabled",
                    onClick = {}
                )
            }
        }
    }
}