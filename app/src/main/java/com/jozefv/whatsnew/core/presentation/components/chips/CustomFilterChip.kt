package com.jozefv.whatsnew.core.presentation.components.chips

import WhatsNewTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle

@Composable
fun CustomFilterChip(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    isEnabled: Boolean = true,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        modifier = modifier,
        colors = FilterChipDefaults.filterChipColors().copy(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            labelColor = MaterialTheme.colorScheme.onSurface,
            leadingIconColor = MaterialTheme.colorScheme.onSurface,
            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        enabled = isEnabled,
        selected = isSelected,
        onClick = { onClick() },
        leadingIcon = {
            leadingIcon?.let { icon ->
                if (isSelected) {
                    Icon(
                        imageVector = icon,
                        contentDescription = icon.name
                    )
                }
            }
        },
        trailingIcon = {
            trailingIcon?.let { icon ->
                    Icon(imageVector = icon, contentDescription = icon.name)
            }
        },
        label = {
            Text(style = CustomStyle.Text.FilterChipText, text = text)
        }
    )
}


@PreviewLightDark
@Composable
private fun CustomFilterChipPreview() {
    WhatsNewTheme {
        Column {
            CustomFilterChip(
                leadingIcon = Icons.Default.Check,
                text = "I am a selected chip",
                isSelected = true,
                onClick = {}
            )
            CustomFilterChip(
                leadingIcon = Icons.Default.Check,
                text = "I am just a chip",
                isSelected = false,
                onClick = {}
            )
            CustomFilterChip(
                text = "I don't have an icon",
                isSelected = true,
                onClick = {}
            )
            CustomFilterChip(
                trailingIcon = Icons.Default.Clear,
                text = "I have a trialing icon",
                isSelected = true,
                onClick = {}
            )
        }
    }
}