package com.example.healthbuddy.exercise

import android.content.Context
import android.widget.TextView
import com.example.healthbuddy.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight

class GraphMarkerView(context: Context, layoutResource: Int, dayText: String, calBurnttext: String) : MarkerView(context, layoutResource) {
    private val markerTextView: TextView = findViewById(R.id.markerTextView)
    private val dayText = dayText
    private val calBurnttext = calBurnttext

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        if (e != null) {
            // Access the data associated with the clicked data point
            val xValue = e.x.toInt()

            // Customize the marker text based on the data
            val markerText = dayText + ": $xValue\n" + calBurnttext + ": \n${e.y}"
            markerTextView.text = markerText
        }

        super.refreshContent(e, highlight)
    }
}