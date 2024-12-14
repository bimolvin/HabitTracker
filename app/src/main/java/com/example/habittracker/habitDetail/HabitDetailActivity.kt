package com.example.habittracker.habitDetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import com.example.habittracker.R
import com.example.habittracker.data.Habit
import com.example.habittracker.habitList.HabitListActivity
import com.example.habittracker.json.convertGoalToInt
import com.github.mikephil.charting.charts.PieChart
import java.text.SimpleDateFormat
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale

const val HABIT_ID = "habit id"

/* Displays habit chosen from list. */
class HabitDetailActivity : AppCompatActivity() {

    private val habitDetailViewModel by viewModels<HabitDetailViewModel> {
        HabitDetailViewModelFactory()
    }
    private val parser = SimpleDateFormat(
        "dd MMM kk:mm", Locale("ru", "RU")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_detail)

        var currentHabitId: Long? = null

        /* Connect variables to UI elements. */
        val habitName: TextView = findViewById(R.id.habit_title)
        val goalTime: TextView = findViewById(R.id.goal_time)
        val startTime: TextView = findViewById(R.id.start_time)
        val finishTime: TextView = findViewById(R.id.finish_time)
        val pieChart: PieChart = findViewById(R.id.pieChart)

        val buttonBack : Button = findViewById(R.id.button_back)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentHabitId = bundle.getLong(HABIT_ID)
        }

        /* If currentHabitId is not null, get corresponding habit and set name, pie chart and
        description */
        currentHabitId?.let {
            val currentHabit = habitDetailViewModel.getHabitForId(it)
            currentHabit?.let { habit ->
                habitName.text = habit.name
                setTextSize(habitName)

                val completedMinutes = countCompletedMinutes(habit)
                val convertedGoal = convertGoalToInt(habit.goal)
                drawPieChart(this, pieChart, convertedGoal, completedMinutes)

                goalTime.text = habit.goal
                startTime.text = formatStart(habit.startedAt)
                finishTime.text = formatFinish(habit.completedAt)
            }
            /* Go back to habit list. */
            buttonBack.setOnClickListener {_ ->
                val intent = Intent(this, HabitListActivity::class.java)
                startActivity(intent)
            }
        }
    }

    /* Makes font size smaller for long habit names. */
    private fun setTextSize(title: TextView) {
        if(title.text.length > 8) {
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 56F)
        }
    }

    /* Returns number of minutes spent on completing the goal.
    * (may not be equal to goal time if stopped earlier or not finished yet) */
    private fun countCompletedMinutes(habit: Habit) : Int {
        var completedMinutes = 0
        if(habit.startedAt != null && habit.completedAt != null) {
            completedMinutes = ChronoUnit.MINUTES.between(
                habit.startedAt?.toInstant(), habit.completedAt?.toInstant())
                .toInt()
        } else if(habit.startedAt != null) {
            completedMinutes = ChronoUnit.MINUTES.between(
                habit.startedAt?.toInstant(), Calendar.getInstance().time.toInstant())
                .toInt()
        }
        return completedMinutes
    }

    /* Returns formatted start time or message about the goal not started yet. */
    private fun formatStart(start: Date?) : String {
        return if(start != null) {
            parser.format(start)
        } else {
            resources.getString(R.string.not_yet_started)
        }
    }

    /* Returns formatted finish time or message about the goal not completed yet. */
    private fun formatFinish(finish: Date?) : String {
        return if(finish != null) {
            parser.format(finish)
        } else {
            resources.getString(R.string.not_yet_finished)
        }
    }
}