package com.example.habittracker.retrofit

import android.util.Log
import com.example.habittracker.data.Habit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

private lateinit var habitsList: List<Habit>

/* Sends network request for fetching habits data. */
fun getHabitsFromUrl() : List<Habit>{
    val habitsApi = RetrofitBuilder.getInstance().create(HabitsApi::class.java)

    /* Launching async coroutine. */
    val deferredResult: Deferred<List<Habit>> = CoroutineScope(Dispatchers.IO).async {
        try {
            /* Launching suspend function. */
            val response = habitsApi.getHabits()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@async response.body()!!.habits
                }
            }
        } catch (e: Exception) {
            Log.e("Retrofit", "Error during network request", e)
        }
        return@async ArrayList()
    }

    /* Getting result from coroutine. */
    runBlocking {
        habitsList = deferredResult.await()
        println("Coroutine result: $habitsList")
    }

    return habitsList
}