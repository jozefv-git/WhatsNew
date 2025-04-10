package com.jozefv.whatsnew.feat_articles.presentation.filter.util

import androidx.annotation.StringRes
import com.jozefv.whatsnew.R
import com.jozefv.whatsnew.feat_articles.domain.filter.FilterCategories

data class FilterRes(@StringRes val filterResource: Int, val filterCategory: FilterCategories)

enum class Filters(val filters: List<FilterRes>) {
    CATEGORY_FILTERS(
        listOf(
            FilterRes(R.string.business, FilterCategories.CATEGORY_FILTERS),
            FilterRes(R.string.entertainment, FilterCategories.CATEGORY_FILTERS),
            FilterRes(R.string.food, FilterCategories.CATEGORY_FILTERS),
            FilterRes(R.string.lifestyle, FilterCategories.CATEGORY_FILTERS),
            FilterRes(R.string.science, FilterCategories.CATEGORY_FILTERS),
            FilterRes(R.string.sports, FilterCategories.CATEGORY_FILTERS),
            FilterRes(R.string.politics, FilterCategories.CATEGORY_FILTERS),
            FilterRes(R.string.crime, FilterCategories.CATEGORY_FILTERS),
            FilterRes(R.string.technology, FilterCategories.CATEGORY_FILTERS),
            FilterRes(R.string.tourism, FilterCategories.CATEGORY_FILTERS),
            FilterRes(R.string.other, FilterCategories.CATEGORY_FILTERS),
            FilterRes(R.string.world, FilterCategories.CATEGORY_FILTERS)
        )
    ),
    COUNTRY_FILTERS(
        listOf(
            FilterRes(R.string.czechia, FilterCategories.COUNTRY_FILTERS),
            FilterRes(R.string.finlad, FilterCategories.COUNTRY_FILTERS),
            FilterRes(R.string.germany, FilterCategories.COUNTRY_FILTERS),
            FilterRes(R.string.poland, FilterCategories.COUNTRY_FILTERS),
            FilterRes(R.string.slovakia, FilterCategories.COUNTRY_FILTERS),
            FilterRes(R.string.uk, FilterCategories.COUNTRY_FILTERS),
            FilterRes(R.string.usa, FilterCategories.COUNTRY_FILTERS)
        )
    ),
    LANGUAGE_FILTERS(
        listOf(
            FilterRes(R.string.czech, FilterCategories.LANGUAGE_FILTERS),
            FilterRes(R.string.slovak, FilterCategories.LANGUAGE_FILTERS),
            FilterRes(R.string.english, FilterCategories.LANGUAGE_FILTERS),
            FilterRes(R.string.finnish, FilterCategories.LANGUAGE_FILTERS),
            FilterRes(R.string.german, FilterCategories.LANGUAGE_FILTERS),
            FilterRes(R.string.polish, FilterCategories.LANGUAGE_FILTERS)
        )
    )
}