package com.example.skinsure.model

data class TextFieldModel(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val confirmPasswordError: String = "",
    val nameError: String = "",
    val isEmailExists: Boolean = false
)
