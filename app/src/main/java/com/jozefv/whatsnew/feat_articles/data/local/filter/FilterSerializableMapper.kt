package com.jozefv.whatsnew.feat_articles.data.local.filter

import com.jozefv.whatsnew.feat_articles.domain.filter.model.Filter
import com.jozefv.whatsnew.feat_articles.domain.filter.FilterCategories
import kotlinx.serialization.Serializable

@Serializable
data class FilterSerializable(val filterCategory: FilterCategories, val filterName: String)

fun Filter.toFilterSerializable(): FilterSerializable {
    return FilterSerializable(
        filterCategory = filterCategory,
        filterName = filterName
    )
}

fun FilterSerializable.toFilter(): Filter {
    return Filter(
        filterCategory = filterCategory,
        filterName = filterName
    )
}
