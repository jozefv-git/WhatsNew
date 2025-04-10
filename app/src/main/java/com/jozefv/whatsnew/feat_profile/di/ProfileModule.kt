package com.jozefv.whatsnew.feat_profile.di

import androidx.room.Room
import com.jozefv.whatsnew.feat_profile.data.database.ArticlesDatabase
import com.jozefv.whatsnew.feat_profile.presentation.ProfileViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            ArticlesDatabase::class.java,
            "articles.db"
        ).build()
    }
    single { get<ArticlesDatabase>().articleDao }

    viewModelOf(::ProfileViewModel)
}