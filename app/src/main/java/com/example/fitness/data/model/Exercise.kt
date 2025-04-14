package com.example.fitness.data.model

import java.io.Serializable

data class Exercise(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val calories: String = "",
    val category_id: Int = 0,
    val image_url: String = "",
    val time: String = "",
    val time_plan: String = "",
    var set_temp : Int = 0,
    var video_url : String = "0",
    val guide_steps: List<GuideStep> = emptyList(),
    val tags: List<Tag> = emptyList()
) : Serializable