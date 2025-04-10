package com.jozefv.whatsnew.feat_articles.presentation.articles.components.list

import WhatsNewTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jozefv.whatsnew.R
import com.jozefv.whatsnew.core.presentation.components.SpacerVerL
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle
import com.jozefv.whatsnew.core.presentation.components.chips.CustomFilterChip
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.lessImportant
import com.jozefv.whatsnew.feat_articles.presentation.filter.model.FilterUi
import com.jozefv.whatsnew.feat_articles.presentation.filter.model.previewFilterState
import com.jozefv.whatsnew.feat_articles.presentation.filter.util.Filters

@Composable
fun TopListFilterSection(
    modifier: Modifier = Modifier,
    searchWithFiltersVisible: Boolean,
    query: String,
    onQueryClick: () -> Unit,
    filtersForDisplay: List<FilterUi>,
    selectedFilter: FilterUi?,
    onFilterClick: (isSelected: Boolean, filterUi: FilterUi) -> Unit
) {
    Column(modifier) {
        if (query.isNotBlank()) {
            if (searchWithFiltersVisible) {
                Text(
                    style = CustomStyle.Text.InfoText.copy(color = MaterialTheme.colorScheme.secondary.lessImportant()),
                    text = stringResource(id = R.string.search_with_filters_enabled)
                )
            }
            CustomFilterChip(
                trailingIcon = Icons.Default.Clear,
                isSelected = true,
                text = query,
                onClick = { onQueryClick() }
            )
        }

        if (filtersForDisplay.isNotEmpty()) {
            Text(
                style = CustomStyle.Text.InfoText.copy(color = MaterialTheme.colorScheme.onSurface.lessImportant()),
                text = stringResource(id = R.string.sort_by)
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                items(filtersForDisplay) { filterUi ->
                    val isSelected = (filterUi == selectedFilter)
                    CustomFilterChip(
                        text = filterUi.filterName,
                        isSelected = isSelected,
                        onClick = {
                            onFilterClick(isSelected, filterUi)
                        })
                }
            }
            SpacerVerL()
        }
    }
}

@PreviewLightDark
@Composable
private fun TopListFilterSectionPreview() {
    val filterState = previewFilterState()
    WhatsNewTheme {
        TopListFilterSection(
            searchWithFiltersVisible = true,
            query = "Dog",
            selectedFilter = FilterUi(
                filterName = stringResource(id = Filters.CATEGORY_FILTERS.filters[1].filterResource),
                filterCategory = Filters.CATEGORY_FILTERS.filters[1].filterCategory
            ),
            filtersForDisplay = filterState.filtersForDisplay,
            onFilterClick = { _, _ -> },
            onQueryClick = {}
        )
    }
}