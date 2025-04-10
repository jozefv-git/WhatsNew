package com.jozefv.whatsnew.core.presentation.components.buttons

import WhatsNewTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun CustomFabButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    isVisible: Boolean = true,
    onclick: () -> Unit
) {
    AnimatedVisibility(modifier = modifier, visible = isVisible) {
        FloatingActionButton(
            modifier = modifier,
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            onClick = { onclick() },
            content = {
                Icon(imageVector = icon, contentDescription = icon.name)
            })
    }
}

@PreviewLightDark
@Composable
private fun CustomRoundedActionIconButtonPreview() {
    WhatsNewTheme {
        CustomFabButton(
            icon = Icons.Default.ArrowUpward,
            onclick = {}
        )
    }
}