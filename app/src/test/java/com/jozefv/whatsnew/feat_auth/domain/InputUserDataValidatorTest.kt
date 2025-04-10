package com.jozefv.whatsnew.feat_auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
// Unit test
class InputUserDataValidatorTest {
    // Subject under the test
    private lateinit var inputUserDataValidator: InputUserDataValidator

    // We need BeforeEach - so re-initialize everything needed for our tests before every test
    // so we always test the default state - as every single test case should run with the same setup
    // we don't want to re-use same instance of the class as it can be potentially
    // changed (its internal state) by some test - which may result in incorrect validation of other tests
    @BeforeEach
    fun setUp() {
        // We don't care what patter validator returns - we are testing unit - not integrated test
        // - so we create stub(double) for that
        // We are not going to test patternValidator as its just simply delegates call to other external dependency
        // so not real testable logic is implemented inside the isEmailValid() function
        // we want to test userDataValidator other functions which involves at least some logic
        val emailPatternValidator = object : EmailPatternValidator {
            override fun isEmailValid(email: String): Boolean {
                return true
            }
        }
        inputUserDataValidator = InputUserDataValidator(emailPatternValidator = emailPatternValidator)
    }

    // Parameterized tests - great for testing bunch of clear inputs and outputs - with single test,
    // we can cover multiple scenarios
    @ParameterizedTest
    @CsvSource(
        "jozef, true",
        "123, true",
        "joz22, true",
        "1joz1, true",
        "2, false",
        "j2, false",
        "j, false",
        "jozef_, false",
        "*joze*, false"
    )
    fun isNickValid(input: String, valid: Boolean) {
        // Input data
        val result = inputUserDataValidator.isNickValid(input)

        assertThat(result).isEqualTo(valid)
    }

    @ParameterizedTest
    @CsvSource(
        "abcde, false",
        "12345, false",
        "a1234, true",
        "1545jJ, true",
        "1ABCDE1, true",
        "a, false",
        "0*, false",
        "12a8D895_, false",
        "+{?____, false",
        "1+/8-ssa, false"
    )
    fun isPasswordValid(password: String, valid: Boolean) {
        val result = inputUserDataValidator.isPasswordValid(password)
        assertThat(result).isEqualTo(valid)
    }
}