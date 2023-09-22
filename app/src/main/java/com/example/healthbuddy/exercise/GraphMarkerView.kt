package com.example.healthbuddy.exercise

import android.content.Context
import android.widget.TextView
import com.example.healthbuddy.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight

class GraphMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
    private val markerTextView: TextView = findViewById(R.id.markerTextView)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        if (e != null) {
            // Access the data associated with the clicked data point
            val xValue = e.x.toInt() + 1 // Assuming x represents the day

            // Customize the marker text based on the data
            val markerText = "Day: $xValue\nCal burnt: ${e.y}"
            markerTextView.text = markerText
        }

        super.refreshContent(e, highlight)
    }
}