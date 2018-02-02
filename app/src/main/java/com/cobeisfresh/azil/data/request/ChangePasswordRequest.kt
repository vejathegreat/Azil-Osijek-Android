package com.cobeisfresh.azil.data.request

/**
 * Created by Zerina Salitrezic on 26/10/2017.
 */

data class ChangePasswordRequest(var oldPassword: String = "",
                                 var newPassword: String = "")