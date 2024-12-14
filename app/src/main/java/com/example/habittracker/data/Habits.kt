package com.example.habittracker.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Habits (
    @Json(name = "habits")
    val habits: List<Habit>
)