package com.example.habittracker.json

import android.net.ParseException
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

var DateTimeAdapter: Any = object : Any() {
    private val simpleDateFormat = getSimpleDateFormat()

    @ToJson
    @Synchronized
    fun dateToJson(d: Date): String {
        return simpleDateFormat.format(d)
    }

    @FromJson
    @Synchronized
    @Throws(ParseException::class)
    fun dateToJson(s: String): Date? {
        return simpleDateFormat.parse(s)
    }
}

fun getSimpleDateFormat() : SimpleDateFormat{
    val pattern = "yyyy-mm-dd'T'HH:mm:ss'Z'"
    return SimpleDateFormat(pattern, Locale("ru", "RU"))
}

/* Converts strings like "15 минут" or "2 часа" to 15 or 120. */
fun convertGoalToInt(goal: String) : Int {
    val goalSplit = goal.split(" ")
    var intGoal: Int = goalSplit[0].toInt()
    if(goalSplit[1][0] == 'ч') {
        intGoal *= 60
    }
    return intGoal
}
