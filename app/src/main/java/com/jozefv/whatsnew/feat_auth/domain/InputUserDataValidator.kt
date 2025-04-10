package com.jozefv.whatsnew.feat_auth.domain

// For simplicity
class InputUserDataValidator(
    private val emailPatternValidator: EmailPatternValidator
) {

    // Min length is 3 and only letters and digits are allowed
    fun isNickValid(nick: String): Boolean {
        return nick.length >= MIN_NICK_LENGTH && nick.all { it.isLetterOrDigit() }
    }

    fun isEmailValid(email: String): Boolean {
        return emailPatternValidator.isEmailValid(email)
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= MIN_PASSWORD_LENGTH
                && password.all { it.isLetterOrDigit() }
                && password.any { it.isLetter() }
                && password.any { it.isDigit() }
    }

    companion object {
        private const val MIN_NICK_LENGTH = 3
        private const val MIN_PASSWORD_LENGTH = 5
    }
}