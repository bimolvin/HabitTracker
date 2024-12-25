package com.example.habittracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.habittracker.retrofit.getHabitsFromUrl

/* Handles operations on habitsLiveData and holds details about it. */
class DataSource {
//    private val initialHabitList = habitList()    // local test data
    private val initialHabitList = getHabitsFromUrl()   // server data
    private val habitsLiveData = MutableLiveData(initialHabitList)

    /* Returns habit given an ID. */
    fun getHabitForId(id: Long): Habit? {
        habitsLiveData.value?.let { habits ->
            return habits.firstOrNull{ it.id == id}
        }
        return null
    }

    /* Returns position at liveData given a habit. */
    private fun getHabitIndex(habit: Habit) : Int {
        habitsLiveData.value?.let { habits ->
            return habits.indexOf(habit)
        }
        return -1
    }

    /* Returns all habits. */
    fun getHabitList(): LiveData<List<Habit>> {
        return habitsLiveData
    }

    /* Edits existing habit at liveData and posts value. */
    fun editHabit(habit: Habit) {
        val currentList = habitsLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList[getHabitIndex(habit)] = habit
            habitsLiveData.postValue(updatedList)
        }
    }

    /* Sends network request for fetching updated data. */
    fun updateData() {
        habitsLiveData.postValue(getHabitsFromUrl())
//        habitsLiveData.postValue(habitList())
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