package com.jozefv.whatsnew.feat_auth.presentation.intro

sealed interface IntroAction {
    data object OnSkipClick: IntroAction
    data object OnSignUpClick: IntroAction
}