package com.jozefv.whatsnew.feat_articles.presentation

import com.jozefv.whatsnew.core.presentation.util.UiText

interface SharedArticlesEvent {
    data object FilterSuccess: SharedArticlesEvent
    data object SearchSuccess: SharedArticlesEvent
    data object SaveSuccess: SharedArticlesEvent
    data object FiveCategoriesSelected: SharedArticlesEvent
    data class ErrorEventShared(val value: UiText): SharedArticlesEvent
}