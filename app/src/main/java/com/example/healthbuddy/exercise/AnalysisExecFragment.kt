package com.example.healthbuddy.exercise

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthbuddy.R
import com.example.healthbuddy.databinding.FragmentAnalysisExecBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class AnalysisExecFragment : Fragment() {

    private var _binding: FragmentAnalysisExecBinding? = null
    // !! --> Tell kotlin that it will never be null
    private val binding get() = _binding!!

    private val execCatList = ArrayList<String>()
    private val execTypeList = ArrayList<String>()
    private lateinit var execAnalysisAdapter: ExecAnalysisAdapter
    private lateinit var execAnalysisGraphAdapter: ExecAnalysisGraphAdapter

    private lateinit var execDataViewModel: ExecDataViewModel

    private val entries = ArrayList<Entry>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnalysisExecBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize ViewModel
        execDataViewModel = ViewModelProvider(this).get(ExecDataViewModel::class.java)
        binding.execDataViewModel = execDataViewModel

        // Recycler View
        execAnalysisAdapter = ExecAnalysisAdapter()
        binding.execAnalysisView.layoutManager = LinearLayoutManager(requireContext())
        binding.execAnalysisView.adapter = execAnalysisAdapter

        // ViewModel
        // Outside conditional check: Let the compiler know that binding.execDataViewModel wont be null
        binding.execDataViewModel?.let { execDataViewModel ->
            execDataViewModel.getAllExecData.observe(viewLifecycleOwner, Observer { userExecData ->
                execAnalysisAdapter.setData(userExecData)
            })

            execDataViewModel.getTotalCaloriesBurnt.observe(viewLifecycleOwner, Observer { totalCaloriesBurntList ->
                // Clear the existing entries before adding new data
                entries.clear()

                // Map the totalCaloriesBurntList to Entry objects and add them to the entries list
                totalCaloriesBurntList.forEachIndexed { index, totalCaloriesBurnt ->
                    entries.add(Entry(totalCaloriesBurnt.day, totalCaloriesBurnt.totalCaloriesBurnt.toFloat()))
                    Log.d("Calories Burnt", totalCaloriesBurnt.totalCaloriesBurnt.toString())
                    Log.d("Day", totalCaloriesBurnt.day.toString())
                }

                val dataSet = LineDataSet(entries, "Calories Burnt")
                val dataSets: ArrayList<ILineDataSet> = ArrayList()
                dataSets.add(dataSet)

                val lineData = LineData(dataSets)

                // Set Description
                binding.execGraph.description.text = "Only approximated value is shown"

                // Set marker view
                val markerView = GraphMarkerView(requireContext(), R.layout.graph_marker_view)
                binding.execGraph.marker = markerView

                // Customize the X-axis
                val xAxis = binding.execGraph.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM // Position of the X-axis labels
                xAxis.setDrawGridLines(false) // Hide grid lines if not needed

                // Define the range of X-axis values (assuming your data covers a range of days)
                val minValue = 1f // Minimum day value
                val maxValue = 31f // Maximum day value

                // Set the minimum and maximum values for the X-axis
                xAxis.axisMinimum = minValue
                xAxis.axisMaximum = maxValue

                // Set the granularity to control the spacing between labels (optional)
                xAxis.granularity = 1f // Spacing of 1 unit between labels (1 day)

                // Customize the Y-axis (left)
                val yAxisLeft = binding.execGraph.axisLeft
                yAxisLeft.setDrawGridLines(false) // Hide grid lines if not needed

                // Customize the Y-axis (right)
                val yAxisRight = binding.execGraph.axisRight
                yAxisRight.isEnabled = false // Disable the right Y-axis

                binding.execGraph.data = lineData
                binding.execGraph.invalidate() // Refresh the chart
            })
        }
        return view
    }

}