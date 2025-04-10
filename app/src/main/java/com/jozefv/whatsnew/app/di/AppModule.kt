package com.jozefv.whatsnew.app.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.jozefv.whatsnew.app.MainViewModel
import com.jozefv.whatsnew.core.domain.LocalArticlesRepository
import com.jozefv.whatsnew.core.domain.util.CoroutineDispatchers
import com.jozefv.whatsnew.core.domain.util.ProductionDispatchers
import com.jozefv.whatsnew.feat_profile.data.RoomLocalArticlesRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    // Koin will automatically provide single instances across the app where needed
    // Define shared prefs module in app module because they are used across whole app
    single<SharedPreferences> {
        EncryptedSharedPreferences(
            androidApplication(),
            "encrypted_auth_prefs",
            MasterKey(
                androidApplication()
            ),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    // This will be accessed across the app -  so we define it in the appModule
    singleOf(::RoomLocalArticlesRepository).bind<LocalArticlesRepository>()
    single<CoroutineDispatchers> { ProductionDispatchers }

    viewModelOf(::MainViewModel)
}