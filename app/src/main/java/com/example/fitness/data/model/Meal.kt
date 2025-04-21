package com.example.fitness.data.model

import java.io.Serializable

data class Meal (
    val id : String? = null,
    val title : String? = null,
    val description : String? = null,
    val image : String? = null,
    val calo : String? = null,
    val canxi : String? = null,
    val protein : String? = null,
    val fat : String? = null,
    val carb : String? = null,
    val iron : String? = null,
    val cholesterol : String? = null,
    val kali : String? = null,
    val magie : String? = null,
    val meal_category_id : String? = null,
    val zinc : String? = null,
) : Serializable