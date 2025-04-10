package com.jozefv.whatsnew.feat_auth.di

import com.jozefv.whatsnew.feat_auth.data.EmailPatternValidatorImpl
import com.jozefv.whatsnew.feat_auth.data.PrefsAuthRepository
import com.jozefv.whatsnew.feat_auth.domain.AuthRepository
import com.jozefv.whatsnew.feat_auth.domain.EmailPatternValidator
import com.jozefv.whatsnew.feat_auth.domain.InputUserDataValidator
import com.jozefv.whatsnew.feat_auth.presentation.login.LoginViewModel
import com.jozefv.whatsnew.feat_auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {

    single<EmailPatternValidator> {
        EmailPatternValidatorImpl
    }
    // Koin will automatically figure out what dependencies InputUserDataValidator needs (EmailPatternValidator)
    // If such instance exists - koin will automatically provide it
    singleOf(::InputUserDataValidator)

    // Shared prefs are declared in App module as they are accessed across the app, so koin has reference to it
    // when creating PrefsAuthRepository
    singleOf(::PrefsAuthRepository).bind<AuthRepository>()

    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
}