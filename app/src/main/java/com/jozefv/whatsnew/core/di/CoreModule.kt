package com.jozefv.whatsnew.core.di

import com.jozefv.whatsnew.core.data.EncryptedSharedPrefSessionStorage
import com.jozefv.whatsnew.core.domain.SessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    // Actual Shared prefs are declared in App module as they are accessed across the app
    singleOf(::EncryptedSharedPrefSessionStorage).bind<SessionStorage>()
}