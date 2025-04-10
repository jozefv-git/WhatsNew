package com.jozefv.whatsnew.core.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle

@Composable
fun CustomAlertDialog(
    modifier: Modifier = Modifier,
    dialogTitle: String,
    dialogText: String,
    dismissButtonText: String = "No",
    confirmButtonText: String = "Yes",
    dialogIcon: @Composable () -> Unit = {},
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val customStyle = CustomStyle.Text
    AlertDialog(
        modifier = modifier,
        title = {
            Text(style = customStyle.DefaultText, text = dialogTitle)
        },
        text = {
            Text(style = customStyle.DefaultText, text = dialogText)
        },
        icon = {
            dialogIcon()
        },
        onDismissRequest = { onDismiss() },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(style = customStyle.ButtonText, text = dismissButtonText)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
            }) {
                Text(style = customStyle.ButtonText, text = confirmButtonText)
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun CustomAlertDialogPreview() {
    CustomAlertDialog(
        dialogTitle = "Logout",
        dialogText = "Are you sure you want to log out?",
        dialogIcon = {
            Icon(Icons.AutoMirrored.Default.Logout, null)
        },
        onDismiss = { },
        onConfirm = {})
}