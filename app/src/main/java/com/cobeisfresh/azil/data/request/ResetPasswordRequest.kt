package com.cobeisfresh.azil.data.request

/**
 * Created by Zerina Salitrezic on 22/08/2017.
 */

data class ResetPasswordRequest(var code: String = "",
                                var email: String = "",
                                var password: String = "")