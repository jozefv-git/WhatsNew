package com.jozefv.whatsnew.feat_articles.presentation.filter.components

import WhatsNewTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle
import com.jozefv.whatsnew.core.presentation.components.SpacerVerXS
import com.jozefv.whatsnew.core.presentation.components.chips.CustomFilterChip
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.lessImportant
import com.jozefv.whatsnew.feat_articles.presentation.filter.util.FilterRes
import com.jozefv.whatsnew.feat_articles.presentation.filter.model.FilterUi
import com.jozefv.whatsnew.feat_articles.presentation.filter.model.previewFilterState
import com.jozefv.whatsnew.feat_articles.presentation.filter.util.Filters

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipFilters(
    modifier: Modifier = Modifier,
    horizontalChipSpacing: Dp = 4.dp,
    verticalChipSpacing: Dp = 4.dp,
    filterDescription: String? = null,
    filtersCategory: String,
    filters: List<FilterRes>,
    selectedFilters: List<FilterUi>,
    onClick: (currentFilter: FilterUi, isSelected: Boolean) -> Unit
) {
    val customStyle = CustomStyle.Text
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            style = customStyle.SectionTitle.copy(MaterialTheme.colorScheme.onBackground),
            text = filtersCategory
        )
        filterDescription?.let {
            SpacerVerXS()
            Text(
                style = customStyle.InfoTextLessImportant.copy(color = MaterialTheme.colorScheme.onBackground.lessImportant()),
                text = it
            )
        }
        SpacerVerXS()
        FlowRow(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(horizontalChipSpacing),
            verticalArrangement = Arrangement.spacedBy(verticalChipSpacing)
        ) {
            filters.forEach { filter ->
                val filterUi =
                    FilterUi(stringResource(id = filter.filterResource), filter.filterCategory)
                val isSelected = selectedFilters.contains(filterUi)
                CustomFilterChip(
                    text = filterUi.filterName,
                    isSelected = isSelected,
                    onClick = {
                        onClick(filterUi, isSelected)
                    })
            }
        }
    }
}

@PreviewLightDark()
@Composable
private fun FilterCategoryPreview() {
    WhatsNewTheme {
        ChipFilters(
            filterDescription = "Search the news articles from a specific countries.",
            filtersCategory = "Countries",
            filters = Filters.COUNTRY_FILTERS.filters,
            selectedFilters = previewFilterState().selectedFilters,
            onClick = { _, _ ->
            }
        )
    }
}

