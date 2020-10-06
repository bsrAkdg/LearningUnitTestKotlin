package com.bsrakdg.testingapp

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {

    @Test
    fun `empty username returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "",
            "123",
            "123"
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `valid username and correctly repeated password returns true`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "Shayna",
            "123",
            "123"
        )

        assertThat(result).isTrue()
    }

    @Test
    fun `username already exists returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "Jane",
            "123",
            "123"
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `empty password returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "Shayna",
            "",
            ""
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `password was repeated incorrectly return false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "Shayna",
            "12345",
            "123456"
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `password contains less than 2 digits return false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "Shayna",
            "Shayna1",
            "Shayna1"
        )

        assertThat(result).isFalse()
    }
}