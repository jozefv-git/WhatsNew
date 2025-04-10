package com.jozefv.whatsnew.feat_articles.di

import com.jozefv.whatsnew.feat_articles.data.remote.articles.HttpClientFactory
import com.jozefv.whatsnew.feat_articles.data.remote.articles.ArticlesRepositoryKtorImpl
import com.jozefv.whatsnew.feat_articles.data.local.filter.SharedPrefFilterStorage
import com.jozefv.whatsnew.feat_articles.data.local.search.SharedPrefSuggestionsStorage
import com.jozefv.whatsnew.feat_articles.domain.filter.FiltersStorage
import com.jozefv.whatsnew.feat_articles.domain.articles.ArticlesRepository
import com.jozefv.whatsnew.feat_articles.domain.search.SuggestionsStorage
import com.jozefv.whatsnew.feat_articles.presentation.SharedArticlesViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module


val newsModule = module {
    single {
        HttpClientFactory.build(CIO.create())
    }

    singleOf(::SharedPrefFilterStorage).bind<FiltersStorage>()
    singleOf(::SharedPrefSuggestionsStorage).bind<SuggestionsStorage>()

    singleOf(::ArticlesRepositoryKtorImpl).bind<ArticlesRepository>()
    viewModelOf(::SharedArticlesViewModel)
}