package com.azmat.testdrivendevelopment.data.models

import android.util.Log
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.lang.Exception

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class AuthDataSource {

    fun login(email: String, password: String): Result<LoggedInUser> {
        Log.d("login", "$email, $password")
        try {
            Log.d("login", "trying")
            TODO("login")
            val loginTask = "hi"
            var user: LoggedInUser? = null
            return if (user != null) {
                Result.Success(user!!)
            } else {
                Result.Error(Exception())
            }
        } catch (e: Throwable) {
            Log.d("login", "${e.localizedMessage}, ${e.cause}, ${e.message}")
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }

    fun register(username: String, email: String, password: String): Result<LoggedInUser> {
        try {

            TODO("register")
            val user = LoggedInUser(email, username)
            Log.d("Register", "registered $email $user")
            return Result.Success(user)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error registering", e))
        }
    }
}