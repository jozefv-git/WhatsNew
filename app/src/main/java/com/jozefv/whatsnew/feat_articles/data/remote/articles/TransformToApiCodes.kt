package com.jozefv.whatsnew.feat_articles.data.remote.articles

object TransformToApiCodes {
    fun countryToApiCode(selectedFilter: String): String {
        return when (selectedFilter) {
            "Czech republic" -> "cz"
            "Finland" -> "fi"
            "Germany" -> "de"
            "Poland" -> "pl"
            "Slovakia" -> "sk"
            "United Kingdom" -> "gb"
            "USA" -> "us"
            else -> ""
        }
    }

    fun categoryToApiCode(selectedFilter: String): String {
        return when (selectedFilter) {
            "Business" -> "business"
            "Entertainment" -> "entertainment"
            "Food" -> "food"
            "Lifestyle" -> "lifestyle"
            "Science" -> "science"
            "Sports" -> "sports"
            "Technology" -> "technology"
            "Tourism" -> "tourism"
            "Other" -> "other"
            "World" -> "world"
            "Politics" -> "politics"
            "Crime" -> "crime"
            else -> ""
        }
    }

    fun languageToApiCode(selectedFilter: String): String {
        return when (selectedFilter) {
            "Czech" -> "cs"
            "Slovak" -> "sk"
            "English" -> "en"
            "Finnish" -> "fi"
            "German" -> "de"
            "Polish" -> "pl"
            else -> ""
        }
    }
}