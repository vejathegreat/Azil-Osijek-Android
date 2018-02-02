package com.cobeisfresh.azil.data.models

import com.cobeisfresh.azil.common.extensions.isValidEmail
import com.cobeisfresh.azil.common.extensions.isValidName
import com.cobeisfresh.azil.common.extensions.isValidPassword
import com.cobeisfresh.azil.data.request.RegisterRequest
import java.io.Serializable

/**
 * Created by Zerina Salitrezic on 09/01/2018.
 */

data class UserRegistrationData(var name: String = "",
                                var email: String = "",
                                var password: String = "",
                                var repeatedPassword: String = "") : Serializable

fun UserRegistrationData.isRegisterFormValid(): Boolean {
    return name.isValidName()
            && email.isValidEmail()
            && password.isValidPassword()
            && password == repeatedPassword
}

fun UserRegistrationData.mapToRegisterRequest(): RegisterRequest {
    return RegisterRequest(name, email, password)
}
