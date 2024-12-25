package com.example.habittracker.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.habittracker.json.getSimpleDateFormat
import java.util.Date

class DataSharedPreferences(private var context: Context) {
    private val prefsSetting = context.getSharedPreferences("databaseInfo", Context.MODE_PRIVATE)
    private var prefs: SharedPreferences =
        context.getSharedPreferences(getSizeInt().toString(), Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = prefs.edit()
    private var indexNow = 0

    private fun getSizeInt(): Int {
        return prefsSetting.getInt("size", 0)
    }

    private fun updatePrefs(index: Int) {
        if (indexNow != index) {
            indexNow = index
            prefs = context.getSharedPreferences(index.toString(), Context.MODE_PRIVATE)
            editor = prefs.edit()
        }
    }

    fun setHabit(habit: Habit) {
        findHabitById(habit.id)
        setCompleted(habit.isCompleted)
        setStartedAt(habit.startedAt)
        setCompletedAt(habit.completedAt)
    }

    fun findHabitById(input: Long): Int? {
        for (index in 1..getSizeInt()) {
            updatePrefs(index)
            if (input == getId()) {
                return index
            }
        }
        return null
    }

    private fun getId(id: Int? = -1): Long? {
        id?.let {
            if (id != -1)
                updatePrefs(id)
            return prefs.getLong("ID", 0)
        }
        return null
    }

    fun getCompleted(id: Int? = -1): Boolean? {
        id?.let {
            if (id != -1)
                updatePrefs(id)
            return prefs.getBoolean("isCompleted", false)
        }
        return null
    }

    private fun setCompleted(input: Boolean, id: Int? = -1) {
        id?.let {
            if (id != -1)
                updatePrefs(id)
            editor.putBoolean("isCompleted", input).apply()
        }
    }

    fun getStartedAt(id: Int? = -1): Date? {
        id?.let {
            if (id != -1)
                updatePrefs(id)
            val start = prefs.getString("startedAt", "")
            Log.d("DS getStartedAt", start.toString())
            return if(!start.isNullOrEmpty()) getSimpleDateFormat().parse(start) else null
        }
        return null
    }

    private fun setStartedAt(input: Date?, id: Int? = -1) {
        id?.let {
            if (id != -1)
                updatePrefs(id)
            if(input != null) {
                Log.d("DS setStartedAt", getSimpleDateFormat().format(input))
                editor.putString("startedAt", getSimpleDateFormat().format(input)).apply()
            } else {
                editor.putString("startedAt", null).apply()
            }
        }
    }

    fun getCompletedAt(id: Int? = -1): Date? {
        id?.let {
            if (id != -1)
                updatePrefs(id)
            val finish = prefs.getString("completedAt", "")
            Log.d("DS getCompletedAt", finish.toString())
            return if(!finish.isNullOrEmpty()) getSimpleDateFormat().parse(finish) else null
        }
        return null
    }

    private fun setCompletedAt(input: Date?, id: Int? = -1) {
        id?.let {
            if (id != -1)
                updatePrefs(id)
            if(input != null) {
                editor.putString("completedAt", getSimpleDateFormat().format(input)).apply()
            } else {
                editor.putString("completedAt", null).apply()
            }
        }
    }
}