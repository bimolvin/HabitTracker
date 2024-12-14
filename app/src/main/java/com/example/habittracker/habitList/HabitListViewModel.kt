package com.example.habittracker.habitList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.data.DataSource

class HabitListViewModel (val dataSource: DataSource) : ViewModel() {
    val habitsLiveData = dataSource.getHabitList()

    /* Queries datasource to send network request. */
    fun update() {
        dataSource.updateData()
    }
}

class HabitsListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitListViewModel(
                dataSource = DataSource.getDataSource()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}