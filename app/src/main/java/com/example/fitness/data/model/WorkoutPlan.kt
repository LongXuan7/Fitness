package com.example.fitness.data.model

import java.io.Serializable

data class WorkoutPlan (
    val id: String? = null,
    val time: String? = null,
    val exercise_id: Int? = null,
    val user_id: String? = null,
    val set : Int? = null,
    val completedSet: Int? = null,
    val progress: Int? = null,
) : Serializable