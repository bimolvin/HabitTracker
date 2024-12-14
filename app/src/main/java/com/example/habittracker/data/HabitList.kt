package com.example.habittracker.data

import com.example.habittracker.json.getSimpleDateFormat

/* Initial data for testing. */
fun habitList() : List<Habit> {
    return listOf(
        Habit(
            id = 1,
            name = "Чтение",
            goal = "30 мин",
            isCompleted = true,
            isStarted = true,
            startedAt = getSimpleDateFormat().parse("2024-10-21T08:00:00Z"),
            completedAt = getSimpleDateFormat().parse("2024-10-21T08:30:00Z")
        ),
        Habit(
            id = 2,
            name = "Спорт",
            goal = "60 мин",
            isCompleted = false,
            isStarted = true,
            startedAt = getSimpleDateFormat().parse("2024-10-22T07:00:00Z"),
            completedAt = null
        ),
        Habit(
            id = 3,
            name = "Медитация",
            goal = "15 мин",
            isCompleted = true,
            isStarted = true,
            startedAt = getSimpleDateFormat().parse("2024-10-23T06:30:00Z"),
            completedAt = getSimpleDateFormat().parse("2024-10-23T06:45:00Z")
        ),
        Habit(
            id = 4,
            name = "Изучение английского",
            goal = "20 мин",
            isCompleted = false,
            isStarted = false,
            startedAt = null,
            completedAt = null
        ),
        Habit(
            id = 5,
            name = "Письмо",
            goal = "10 мин",
            isCompleted = true,
            isStarted = true,
            startedAt = getSimpleDateFormat().parse("2024-10-24T09:15:00Z"),
            completedAt = getSimpleDateFormat().parse("2024-10-24T09:25:00Z")
        )
    )
}