package com.example.habittracker.habitList

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.data.DataSharedPreferences
import com.example.habittracker.data.Habit
import com.example.habittracker.habitDetail.HABIT_ID
import com.example.habittracker.habitDetail.HabitDetailActivity
import com.example.habittracker.habitDetail.HabitDetailViewModel
import com.example.habittracker.habitDetail.HabitDetailViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.fixedRateTimer


/* Main Activity: displays list of all habits. */
class HabitListActivity : AppCompatActivity() {
    private val habitsListViewModel by viewModels<HabitListViewModel> {
        HabitsListViewModelFactory()
    }
    private val habitDetailViewModel by viewModels<HabitDetailViewModel> {
        HabitDetailViewModelFactory()
    }
    private lateinit var prefs: DataSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = DataSharedPreferences(this)
//        setPrefs()
        restoreFromPrefs()

        /* Forming recycler view. */
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val onClick: (Habit) -> Unit = { habit ->
            adapterOnClick(habit)
        }

        val onCheck: (Habit) -> Unit = { habit ->
            adapterOnCheck(habit)
        }

        val habitsAdapter = HabitsAdapter(onClick, onCheck)
        recyclerView.adapter = habitsAdapter

        habitsListViewModel.habitsLiveData.observe(this) {
            if (it.isNotEmpty()) {
                // Updating adapter with new data
                habitsAdapter.submitList(it as MutableList<Habit>)
            } else {
                // No data received
                Snackbar.make(
                    findViewById(R.id.root_container_view),
                    R.string.error_fetch,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        /* Updating liveData every 90 seconds. */
        CoroutineScope(Dispatchers.IO).launch {
            fixedRateTimer("timer", false, 60_000.toLong(), 90_000) {
                Snackbar.make(
                    findViewById(R.id.root_container_view),
                    R.string.update_fetch,
                    Snackbar.LENGTH_SHORT
                ).show()
                habitsListViewModel.update()
                // reload page to trigger restoring data from preferences
                reload()
            }
        }
    }

    /* Sets initial data to Shared Preferences. */
    private fun setPrefs() {
        val data = habitsListViewModel.habitsLiveData.value
        if (data != null) {
            for(i in data.indices) {
                prefs.setHabit(data[i])
            }
        }
    }

    /* Gets changed data from Shared Preferences. */
    private fun restoreFromPrefs() {
        val data = habitsListViewModel.habitsLiveData.value
        if (data != null) {
            for(i in data.indices) {
                val updatedHabit: Habit = data[i]
                val prefHabitId = prefs.findHabitById(updatedHabit.id)
                prefHabitId?.let {
                    updatedHabit.isCompleted = prefs.getCompleted() ?: false
                    updatedHabit.startedAt = prefs.getStartedAt()
                    updatedHabit.completedAt = prefs.getCompletedAt()
                    habitDetailViewModel.editHabit(updatedHabit)
                }
            }
        }
    }

    /* Opens HabitDetailActivity when RecyclerView item was clicked. */
    private fun adapterOnClick(habit: Habit) {
        val intent = Intent(this, HabitDetailActivity()::class.java)
        intent.putExtra(HABIT_ID, habit.id)
        startActivity(intent)
    }

    /* Saves changes to Shared Preferences when checkbox was clicked. */
    private fun adapterOnCheck(habit: Habit) {
        prefs.setHabit(habit)
    }

    private fun reload() {
        val refresh = Intent(this, HabitListActivity::class.java)
        startActivity(refresh)
        this.finish()
    }
}