package com.example.habittracker.habitDetail

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import androidx.core.content.ContextCompat
import com.example.habittracker.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

fun drawPieChart(context: Context, pieChart: PieChart, goal: Int, completed: Int) {
    // on below line we are setting user percent value,
    // setting description as enabled and offset for pie chart
    pieChart.setUsePercentValues(true)
    pieChart.description.isEnabled = false

    // on below line we are setting drag for our pie chart
    pieChart.dragDecelerationFrictionCoef = 0.95f

    // on below line we are setting hole
    // and hole color for pie chart
    pieChart.isDrawHoleEnabled = true
    pieChart.setHoleColor(ContextCompat.getColor(context, R.color.md_theme_surface))

    // on  below line we are setting hole radius
    pieChart.holeRadius = 70f

    // on below line we are setting center text
    pieChart.setDrawCenterText(true)
    val bmi = "$completed/$goal"
    pieChart.centerText = bmi
    pieChart.setCenterTextSize(46f)
    pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
    pieChart.setCenterTextColor(ContextCompat.getColor(context, R.color.md_theme_tertiary))

    // on below line we are setting
    // rotation for our pie chart
    pieChart.rotationAngle = -90f

    // on below line we are setting animation for our pie chart
    pieChart.animateY(1400, Easing.EaseInOutQuad)

    // on below line we are disabling our legend for pie chart
    pieChart.legend.isEnabled = false

    // on below line we are setting shadow
    pieChart.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    pieChart.renderer.paintRender.setShadowLayer(10f, 0f, 6f, Color.GRAY)

    // on below line we are creating array list and
    // adding data to it to display in pie chart
    val completedEntry = countEntryPerc(goal, completed)

    val entries: ArrayList<PieEntry> = ArrayList()
    entries.add(PieEntry(completedEntry.toFloat()))
    entries.add(PieEntry((100 - completedEntry).toFloat()))

    // on below line we are setting pie data set
    val dataSet = PieDataSet(entries, null)

    // add a lot of colors to list
    val colors: ArrayList<Int> = ArrayList()
    colors.add(ContextCompat.getColor(context, R.color.md_theme_tertiaryContainer))
    colors.add(ContextCompat.getColor(context, R.color.md_theme_surfaceDim))

    // on below line we are setting colors.
    dataSet.colors = colors

    // on below line we are setting pie data set
    val data = PieData(dataSet)
    data.setValueTextColor(Color.TRANSPARENT)
    pieChart.data = data

    // undo all highlights
    pieChart.highlightValues(null)

    // loading chart
    pieChart.invalidate()
}

private fun countEntryPerc(goal: Int, completed: Int) : Int {
    return if(completed >= goal) 100 else if(completed > 0) goal / completed * 100 else 0
}