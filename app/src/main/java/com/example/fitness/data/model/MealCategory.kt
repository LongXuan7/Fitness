package com.example.fitness.data.model

import java.io.Serializable

data class MealCategory (
    val id: String? = null,
    val title: String? = null,
    val image: String? = null,
) : Serializable