package com.jozefv.whatsnew.core.presentation.components

import WhatsNewTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.lessProminent

@Composable
fun CustomSwitch(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val customStyle = CustomStyle.Text
    Row(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                style = customStyle.InfoText.copy(color = MaterialTheme.colorScheme.onBackground),
                text = title
            )
            SpacerVerXS()
            Text(
                style = customStyle.InfoTextLessImportant.copy(color = MaterialTheme.colorScheme.onBackground),
                text = description
            )
        }
        SpacerHorL()
        Switch(
            colors = SwitchDefaults.colors().copy(
                checkedBorderColor = MaterialTheme.colorScheme.primary,
                checkedThumbColor = MaterialTheme.colorScheme.secondary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                uncheckedBorderColor = MaterialTheme.colorScheme.onSurface,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainer,
                disabledCheckedBorderColor = MaterialTheme.colorScheme.primary.lessProminent(),
                disabledCheckedThumbColor = MaterialTheme.colorScheme.secondary.lessProminent(),
                disabledCheckedTrackColor = MaterialTheme.colorScheme.primaryContainer.lessProminent(),
                disabledUncheckedBorderColor = MaterialTheme.colorScheme.onSurface.lessProminent(),
                disabledUncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant.lessProminent(),
                disabledUncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainer.lessProminent(),
            ),
            enabled = enabled,
            checked = checked,
            onCheckedChange = {
                onCheckedChange(it)
            })
    }
}

@PreviewLightDark
@Composable
private fun CustomSwitchPreview() {
    WhatsNewTheme {
        CustomSwitch(
            title = "Custom switch",
            description = "Here will be more descriptive text about the switch functionalities",
            checked = true,
            onCheckedChange = {}
        )
    }
}