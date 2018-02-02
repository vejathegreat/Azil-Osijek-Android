package com.cobeisfresh.azil.data.models

import com.cobeisfresh.azil.common.extensions.isValidCode
import com.cobeisfresh.azil.common.extensions.isValidPassword
import com.cobeisfresh.azil.data.request.ResetPasswordRequest

/**
 * Created by Zerina Salitrezic on 10/01/2018.
 */

class ResetPasswordData(var code: String = "",
                        var email: String = "",
                        var password: String = "",
                        var retypedPassword: String = "")

fun ResetPasswordData.isResetPasswordFormValid(): Boolean {
    return code.isValidCode()
            && password.isValidPassword()
            && password == retypedPassword
}

fun ResetPasswordData.mapToResetPasswordRequest(): ResetPasswordRequest {
    return ResetPasswordRequest(code, email, password)
}
