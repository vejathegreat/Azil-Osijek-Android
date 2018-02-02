package com.cobeisfresh.azil.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Zerina Salitrezic on 21/08/2017.
 */

data class DogModel(
        @SerializedName("_id")
        val id: String = "",
        val name: String = "",
        val age: String = "",
        val gender: String = "",
        val size: String = "",
        val shortDescription: String = "",
        val photo: List<String> = listOf(),
        var isFavorite: Boolean = false) : Serializable