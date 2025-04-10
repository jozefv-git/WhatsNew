package com.jozefv.whatsnew.feat_articles.domain.filter.model

import com.jozefv.whatsnew.feat_articles.domain.filter.FilterCategories

data class Filter(val filterCategory: FilterCategories, val filterName: String)
