package com.jozefv.whatsnew.app.di

import android.app.Application
import com.jozefv.whatsnew.feat_auth.di.authModule
import com.jozefv.whatsnew.core.di.coreModule
import com.jozefv.whatsnew.feat_articles.di.newsModule
import com.jozefv.whatsnew.feat_profile.di.profileModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WhatsNewApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Let koin know about our app context, so it can be used to create dependencies
            androidContext(this@WhatsNewApp)
            // List of modules what we need
            modules(
                appModule,
                authModule,
                newsModule,
                coreModule,
                profileModule
            )
        }
    }
}