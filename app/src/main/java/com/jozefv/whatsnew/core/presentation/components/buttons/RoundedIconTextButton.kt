package com.jozefv.whatsnew.core.presentation.components.buttons

import WhatsNewTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle
import com.jozefv.whatsnew.core.presentation.components.SpacerHorM
import com.jozefv.whatsnew.core.presentation.model.IconTextButton
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.lessProminent
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.paddingS

fun Modifier.centerForCircleLayout(): Modifier {
    return layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        // Get max of the current composable
        val diameter = maxOf(placeable.height, placeable.width)
        layout(
            height = diameter,
            width = diameter,
            placementBlock = {
                placeable.placeRelative(
                    x = (diameter - placeable.width) / 2,
                    y = (diameter - placeable.height) / 2
                )
            }
        )
    }
}

@Composable
fun RoundedIconTextButton(
    modifier: Modifier = Modifier,
    iconTextButton: IconTextButton
) {
    val background = if (iconTextButton.unavailable || !iconTextButton.isEnabled) {
        Color.Unspecified
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }
    val contentColor = if (iconTextButton.unavailable || !iconTextButton.isEnabled) {
        MaterialTheme.colorScheme.onSecondaryContainer.lessProminent()
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }
    Box(
        modifier = modifier
            .background(color = background, CircleShape)
            .centerForCircleLayout()
            .clip(CircleShape)
            .then(
                if (iconTextButton.isEnabled && !iconTextButton.isLoading) {
                    Modifier.clickable { iconTextButton.onAction() }
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.paddingS(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!iconTextButton.isLoading) {
                Icon(
                    tint = contentColor,
                    imageVector = iconTextButton.icon,
                    contentDescription = iconTextButton.icon.name
                )
            } else {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = contentColor)
            }
            SpacerHorM()
            Text(
                style = CustomStyle.Text.InfoText.copy(color = contentColor),
                text = iconTextButton.text
            )
        }
    }
}


@PreviewLightDark
@Composable
private fun IconTextButtonPreview() {
    WhatsNewTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            RoundedIconTextButton(
                iconTextButton = IconTextButton(
                    unavailable = true,
                    icon = Icons.Default.Delete,
                    text = "Delete",
                    onAction = {

                    })
            )
            RoundedIconTextButton(
                iconTextButton = IconTextButton(
                    isEnabled = false,
                    icon = Icons.Default.Delete,
                    text = "Disabled",
                    onAction = {

                    })
            )
            RoundedIconTextButton(
                iconTextButton = IconTextButton(
                    icon = Icons.Default.Share,
                    text = "Share",
                    onAction = {

                    })
            )
            RoundedIconTextButton(
                iconTextButton = IconTextButton(
                    icon = Icons.Default.Save,
                    text = "Save",
                    isLoading = true,
                    onAction = {}
                )
            )
        }
    }
}