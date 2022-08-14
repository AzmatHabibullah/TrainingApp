package com.azmat.testdrivendevelopment.ui.auth.register

data class RegisterResult(
    val success: RegisteredUserView? = null, // todo add confirmation email and delineate
    val error: Int? = null
)