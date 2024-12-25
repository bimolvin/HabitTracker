package com.example.habittracker.habitDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.data.DataSource
import com.example.habittracker.data.Habit

class HabitDetailViewModel(private val dataSource: DataSource) : ViewModel() {
    /* Queries datasource to returns a habit that corresponds to an id. */
    fun getHabitForId(id: Long) : Habit? {
        return dataSource.getHabitForId(id)
    }

    /* Queries datasource to edit a habit. */
    fun editHabit(habit: Habit) {
        dataSource.editHabit(habit)
    }
}

class HabitDetailViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitDetailViewModel(
                dataSource = DataSource.getDataSource()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}