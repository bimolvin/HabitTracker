package com.example.habittracker.retrofit

import com.example.habittracker.data.Habits
import retrofit2.Response
import retrofit2.http.GET

interface HabitsApi {
    @GET("/habits")
    suspend fun getHabits() : Response<Habits>
}