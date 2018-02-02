package com.cobeisfresh.azil.data.models

import com.cobeisfresh.azil.common.extensions.isValidPassword
import com.cobeisfresh.azil.data.request.ChangePasswordRequest

/**
 * Created by Zerina Salitrezic on 10/01/2018.
 */

data class ChangePasswordData(var oldPassword: String = "",
                              var newPassword: String = "",
                              var retypedNewPassword: String = "")

fun ChangePasswordData.isChangePasswordFormValid(): Boolean {
    return oldPassword.isValidPassword()
            && newPassword.isValidPassword()
            && newPassword == retypedNewPassword
}

fun ChangePasswordData.mapToChangePasswordRequest(): ChangePasswordRequest {
    return ChangePasswordRequest(oldPassword, newPassword)
}
