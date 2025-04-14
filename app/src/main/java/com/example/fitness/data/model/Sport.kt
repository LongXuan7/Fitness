package com.example.fitness.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Sport (
    val id: String? = null,
    val title: String? = null,
    val image: String? = null,
    val description: String? = null,
    val thumnal: String? = null,
    @SerializedName("sport_category_id")
    val sportCategoryId : String? = null,
    val benefit: List<Benefit>? = null,
    val impact: List<Impact>? = null,
) : Serializable {

    data class Benefit (
        val description: String? = null,
    ) : Serializable

    data class Impact (
        val description: String? = null,
    ) : Serializable

}

