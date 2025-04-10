package com.jozefv.whatsnew.feat_articles.presentation.search.model

import com.jozefv.whatsnew.feat_articles.presentation.SearchState

val previewSearchState = SearchState(
    searchWithFilters = true,
    query = "Dog",
    searchExactPhrases = true,
    querySuggestions = listOf("Cars", "Dogs", "Hot-Dog", "Hockey", "Sauna")
)