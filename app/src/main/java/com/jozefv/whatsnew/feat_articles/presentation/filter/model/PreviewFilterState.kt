package com.jozefv.whatsnew.feat_articles.presentation.filter.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jozefv.whatsnew.feat_articles.presentation.FilterState
import com.jozefv.whatsnew.feat_articles.presentation.filter.util.Filters

@Composable
fun previewFilterState(): FilterState {
    return FilterState(
        filtersForDisplay = Filters.CATEGORY_FILTERS.filters.take(5).map {
            FilterUi(
                filterName = stringResource(id = it.filterResource),
                filterCategory = it.filterCategory
            )
        },
        selectedFilters = Filters.COUNTRY_FILTERS.filters.take(3).map {
            FilterUi(
                filterName = stringResource(id = it.filterResource),
                filterCategory = it.filterCategory
            )
        }
    )
}