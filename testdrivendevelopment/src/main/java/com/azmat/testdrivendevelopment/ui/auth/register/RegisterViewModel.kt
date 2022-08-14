package com.azmat.testdrivendevelopment.ui.auth.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.azmat.testdrivendevelopment.R
import com.azmat.testdrivendevelopment.data.models.AuthDataSource
import com.azmat.testdrivendevelopment.ui.auth.login.LoggedInUserView
import com.azmat.testdrivendevelopment.ui.auth.login.LoginFormState
import com.azmat.testdrivendevelopment.ui.auth.login.LoginResult
import com.azmat.testdrivendevelopment.data.models.AuthRepository
import com.azmat.testdrivendevelopment.data.models.Result

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _registerForm = MutableLiveData<LoginFormState>()
    val registerFormState: LiveData<LoginFormState> = _registerForm

    private val _registerResult = MutableLiveData<LoginResult>()
    val registerResult: LiveData<LoginResult> = _registerResult


    fun registerDataChanged(email: String, username: String, password: String) {
        if (!isEmailValid(email)) {
            _registerForm.value = LoginFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _registerForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else if (!isUsernameValid(username)) {
            _registerForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else {
            _registerForm.value = LoginFormState(isDataValid = true)
        }
    }

    fun register(username: String, email:String, password: String) {
        var result = authRepository.register(username, email, password)

        if (result is Result.Success) {
            _registerResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _registerResult.value = LoginResult(error = R.string.login_failed)
        }
    }


    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isUsernameValid(username: String): Boolean {
        return username.length > 6
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }


    class RegisterViewModelFactory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                return RegisterViewModel(
                    authRepository = AuthRepository(
                        dataSource = AuthDataSource()
                    )
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}