package com.jozefv.whatsnew.feat_articles.data.remote.articles

object ApiDefaults {
    const val BASE_URL = "https://newsdata.io/api/1/"

    // Max 10 articles per query, should contain image and remove duplicities
    // Other parameters such as language or categories and regions are not restricted
    const val ARTICLES_PER_QUERY = "10"
    const val DEFAULT_URL = "latest?size=$ARTICLES_PER_QUERY&image=1&removeduplicate=1"
    const val OPTIONAL_NEXT_PAGE_PARAM = "&page="
    const val OPTIONAL_COUNTRY_PARAM = "&country="

    // Category and exclude category cannot be combined - api requirement
    const val OPTIONAL_CATEGORY_PARAM = "&category="
    const val OPTIONAL_EXCLUDE_CATEGORY_PARAM = "&excludecategory="
    const val OPTIONAL_LANGUAGE_PARAM = "&language="
    const val OPTIONAL_Q_IN_TITLE_PARAM = "&qintitle="
    const val OPTIONAL_Q_PARAM = "&q="
}