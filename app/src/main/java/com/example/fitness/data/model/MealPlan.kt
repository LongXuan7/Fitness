package com.example.fitness.data.model

data class MealPlan(
    var breakfast: Map<String, MealItem>? = null,
    var lunch: Map<String, MealItem>? = null,
    var dinner: Map<String, MealItem>? = null
)

data class MealItem(
    var nutrition_id: String? = null,
    var gram : Int? = null
)
