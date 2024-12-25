package com.example.habittracker.habitList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.data.Habit
import com.example.habittracker.json.convertGoalToInt
import java.util.Calendar

class HabitsAdapter(private val onClick: (Habit) -> Unit, private val onCheck: (Habit) -> Unit) :
    ListAdapter<Habit, HabitsAdapter.HabitViewHolder>(HabitDiffCallback) {

    /* ViewHolder for Habit, takes in the inflated view and the onClick behavior. */
    class HabitViewHolder(itemView: View, onClick: (Habit) -> Unit, onCheck: (Habit) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val habitTextView: TextView = itemView.findViewById(R.id.habit)
        private val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
        private var currentHabit: Habit? = null

        init {
            itemView.setOnClickListener {
                currentHabit?.let {
                    onClick(it)
                }
            }

            /* Sets habit goal completed/uncompleted if checkbox was clicked. */
            checkbox.setOnClickListener {
                currentHabit?.let {
                    it.isCompleted = !it.isCompleted
                    if(it.isCompleted) {
                        val calendar = Calendar.getInstance()
                        it.completedAt = calendar.time // set current time as finish time

                        // if start time wasn't set, calculate it as (finish_time - goal_time)
                        if(it.startedAt == null) {
                            calendar.add(Calendar.MINUTE, -convertGoalToInt(it.goal))
                            it.startedAt = calendar.time
                        }
                    } else {
                        it.completedAt = null
                    }
                    onCheck(it)
                }
            }
        }

        /* Bind habit name. */
        fun bind(habit: Habit) {
            currentHabit = habit
            habitTextView.text = habit.name

            // set checkbox as checked if goal was completed
            if(habit.isCompleted) {
                checkbox.isChecked = true
            }
        }
    }

    /* Creates and inflates view and return HabitViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.habit_item, parent, false)
        return HabitViewHolder(view, onClick, onCheck)
    }

    /* Gets current habit and uses it to bind view. */
    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = getItem(position)
        holder.bind(habit)
    }
}

object HabitDiffCallback : DiffUtil.ItemCallback<Habit>() {
    override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem.id == newItem.id
    }
}