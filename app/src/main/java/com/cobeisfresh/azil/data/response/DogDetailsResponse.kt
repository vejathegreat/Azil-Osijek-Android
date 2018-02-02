package com.cobeisfresh.azil.data.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Zerina Salitrezic on 22/08/2017.
 */

data class DogDetailsResponse(@SerializedName("_id")
                              val id: String = "",
                              val name: String = "",
                              val gender: String = "",
                              val age: String = "",
                              val size: String = "",
                              val longDescription: String = "",
                              val photo: List<String> = listOf(),
                              val isFavorite: Boolean = false) : Serializable