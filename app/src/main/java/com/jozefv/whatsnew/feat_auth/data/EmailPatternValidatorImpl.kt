package com.jozefv.whatsnew.feat_auth.data

import android.util.Patterns
import com.jozefv.whatsnew.feat_auth.domain.EmailPatternValidator

// Data layer as it's includes android dependencies

data object EmailPatternValidatorImpl : EmailPatternValidator {
    override fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}