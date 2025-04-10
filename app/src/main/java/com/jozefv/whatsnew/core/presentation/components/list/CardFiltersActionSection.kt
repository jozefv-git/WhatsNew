package com.jozefv.whatsnew.core.presentation.components.list

import WhatsNewTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jozefv.whatsnew.core.presentation.components.SpacerHorL
import com.jozefv.whatsnew.core.presentation.components.SpacerHorM
import com.jozefv.whatsnew.core.presentation.components.SpacerVerL
import com.jozefv.whatsnew.core.presentation.components.chips.CustomInfoChip
import com.jozefv.whatsnew.core.presentation.components.buttons.RoundedIconTextButton
import com.jozefv.whatsnew.core.presentation.model.IconTextButton

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CardFiltersActionSection(
    modifier: Modifier = Modifier,
    clickable: Boolean = false,
    notExpandedFiltersMaxLines: Int = 3,
    expandedFiltersMaxLines: Int = Int.MAX_VALUE,
    leadingIcon: IconTextButton,
    trailingIcon: IconTextButton,
    filters: List<String>
) {
    var expandFilters by remember {
        mutableStateOf(false)
    }
    Row(
        modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier
                .weight(1f)
                .run {
                    if (clickable) {
                        clickable {
                            expandFilters = !expandFilters
                        }
                    } else this
                }) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                maxLines = if (expandFilters) expandedFiltersMaxLines else notExpandedFiltersMaxLines
            ) {
                filters.forEach {
                    CustomInfoChip(text = it)
                }
            }
            if (clickable) {
                if (expandFilters) {
                    Icon(
                        tint = MaterialTheme.colorScheme.secondary,
                        imageVector = Icons.Default.ArrowDropUp,
                        contentDescription = Icons.Default.ArrowDropUp.name
                    )
                } else {
                    Icon(
                        tint = MaterialTheme.colorScheme.secondary,
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = Icons.Default.ArrowDropDown.name
                    )
                }
            }
        }
        AnimatedVisibility(visible = !expandFilters) {
            Row {
                SpacerHorL()
                RoundedIconTextButton(iconTextButton = leadingIcon)
                SpacerHorM()
                RoundedIconTextButton(iconTextButton = trailingIcon)
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun CardFilterActionSectionPreview() {
    WhatsNewTheme {
        Column {
            CardFiltersActionSection(
                leadingIcon = IconTextButton(
                    icon = Icons.Default.Save,
                    text = "Save",
                    onAction = {}),
                trailingIcon = IconTextButton(
                    icon = Icons.Default.Share,
                    text = "Share",
                    onAction = {}),
                filters = listOf("Cats", "Dogs", "Cars", "Other", "World")
            )
        SpacerVerL()
        Column {
            CardFiltersActionSection(
                notExpandedFiltersMaxLines = 1,
                clickable = true,
                leadingIcon = IconTextButton(
                    icon = Icons.Default.Save,
                    text = "Save",
                    onAction = {}),
                trailingIcon = IconTextButton(
                    icon = Icons.Default.Share,
                    text = "Share",
                    onAction = {}),
                filters = listOf("Cats", "Dogs", "Cars", "Other", "World")
            )
        }
    }
}
}