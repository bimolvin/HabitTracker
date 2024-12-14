package com.example.habittracker.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class Habit (
    @Json(name = "id")
    val id: Long,

    @Json(name = "name")
    val name: String,

    @Json(name = "goal")
    var goal: String,

    @Json(name = "completed")
    var isCompleted: Boolean,

    @Json(name = "started")
    val isStarted: Boolean,

    @Json(name = "started_at")
    var startedAt: Date?,

    @Json(name = "completed_at")
    var completedAt: Date?
)