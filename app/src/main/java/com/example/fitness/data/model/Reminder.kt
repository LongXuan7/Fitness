package com.example.fitness.data.model

import java.io.Serializable

data class Reminder (
    val id: String? = null,
    val title: String? = null,
    val time: String? = null,
    val user_id: String? = null,
    val status: Boolean = false
) : Serializable {

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "title" to title,
            "time" to time,
            "user_id" to user_id,
            "status" to status
        )
    }
}