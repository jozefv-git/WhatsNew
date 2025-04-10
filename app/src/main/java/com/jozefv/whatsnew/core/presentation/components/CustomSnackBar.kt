package com.jozefv.whatsnew.core.presentation.components

import WhatsNewTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jozefv.whatsnew.core.presentation.components.buttons.CustomTextButton
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.horizontalPaddingM
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.verticalPaddingL

@Composable
fun CustomSnackBar(
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    snackBarData: SnackbarData,
) {
    val customStyle = CustomStyle.Text
    Snackbar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.inverseSurface,
        contentColor = MaterialTheme.colorScheme.inverseOnSurface,
        action = {
            snackBarData.visuals.actionLabel?.let {
                CustomTextButton(
                    colors = ButtonDefaults.textButtonColors().copy(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    text = it,
                    onClick = {
                        snackBarData.performAction()
                    })
            }
        }, content = {
            Text(
                style = customStyle.InfoText,
                text = snackBarData.visuals.message,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}

@PreviewLightDark
@Composable
private fun CustomSnackBarPreview() {
    WhatsNewTheme {
        val snackBarHostState = remember {
            SnackbarHostState()
        }

        CustomScaffold(
            snackBarHost = {
                SnackbarHost(
                    modifier = Modifier
                        .horizontalPaddingM()
                        .verticalPaddingL(),
                    hostState = snackBarHostState,
                    snackbar = { snackBarData ->
                        CustomSnackBar(snackBarData = snackBarData)
                    })
            }, content = {
                LaunchedEffect(key1 = true) {
                    snackBarHostState.showSnackbar(
                        message = "Snack bar message",
                        actionLabel = "Action"
                    )
                }
            })
    }
}