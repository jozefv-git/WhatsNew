package com.jozefv.whatsnew.feat_articles.presentation.filter.model

import com.jozefv.whatsnew.feat_articles.domain.filter.model.Filter
import com.jozefv.whatsnew.feat_articles.domain.filter.FilterCategories


data class FilterUi(val filterName: String, val filterCategory: FilterCategories)

fun FilterUi.toFilter(): Filter {
    return Filter(
        filterCategory = filterCategory,
        filterName = filterName
    )
}

fun Filter.toFilterUi(): FilterUi {
    return FilterUi(
        filterCategory = filterCategory,
        filterName = filterName
    )
}