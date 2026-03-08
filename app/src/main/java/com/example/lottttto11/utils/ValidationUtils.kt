package com.example.lottttto11.utils

import android.util.Patterns
import java.util.regex.Pattern

object ValidationUtils {
    private val strongRegex = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\\$%^&*\\(\\)_+=\\-\\[\\]{}|;:'\",.<>/?]).{8,}$"
    )
    private val mediumRegex = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$"
    )

    fun getPasswordStrength(password: String): String {
        return when {
            strongRegex.matcher(password).matches() -> "Strong"
            mediumRegex.matcher(password).matches() -> "Medium"
            else -> "Weak"
        }
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
