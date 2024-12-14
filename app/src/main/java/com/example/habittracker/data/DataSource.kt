package com.example.habittracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.habittracker.retrofit.getHabitsFromUrl

/* Handles operations on habitsLiveData and holds details about it. */
class DataSource {
//    private val initialHabitList = habitList()
    private val initialHabitList = getHabitsFromUrl()
    private val habitsLiveData = MutableLiveData(initialHabitList)

    /* Returns habit given an ID. */
    fun getHabitForId(id: Long): Habit? {
        habitsLiveData.value?.let { habits ->
            return habits.firstOrNull{ it.id == id}
        }
        return null
    }

    /* Returns all habits. */
    fun getHabitList(): LiveData<List<Habit>> {
        return habitsLiveData
    }

    /* Sends network request for fetching updated data. */
    fun updateData() {
        habitsLiveData.postValue(getHabitsFromUrl())
    }

    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSource(): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource()
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}