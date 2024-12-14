package com.example.habittracker.habitList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.data.Habit
import com.example.habittracker.habitDetail.HABIT_ID
import com.example.habittracker.habitDetail.HabitDetailActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Forming recycler view. */
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val habitsAdapter = HabitsAdapter { habit -> adapterOnClick(habit) }
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

        /* Updating liveData every 30 seconds. */
        CoroutineScope(Dispatchers.IO).launch {
            fixedRateTimer("timer", false, 30_000.toLong(), 30_000) {
                Snackbar.make(
                    findViewById(R.id.root_container_view),
                    R.string.update_fetch,
                    Snackbar.LENGTH_SHORT
                ).show()
                habitsListViewModel.update()
            }
        }
    }

    /* Opens HabitDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(habit: Habit) {
        val intent = Intent(this, HabitDetailActivity()::class.java)
        intent.putExtra(HABIT_ID, habit.id)
        startActivity(intent)
    }
}