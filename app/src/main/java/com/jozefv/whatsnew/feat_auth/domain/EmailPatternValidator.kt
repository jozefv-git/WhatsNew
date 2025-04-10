package com.jozefv.whatsnew.feat_auth.domain

 interface EmailPatternValidator {
    fun isEmailValid(email: String): Boolean
}