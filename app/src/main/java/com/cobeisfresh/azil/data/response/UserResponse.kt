package com.cobeisfresh.azil.data.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Zerina Salitrezic on 22/08/2017.
 */

data class UserResponse(@SerializedName("_id")
                        var id: String = "",
                        var name: String = "",
                        var email: String = "",
                        var profileImage: String = "",
                        var isFacebookUser: Boolean = false) : Serializable