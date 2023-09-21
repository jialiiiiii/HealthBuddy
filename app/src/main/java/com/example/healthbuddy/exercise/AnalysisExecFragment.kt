package com.example.healthbuddy.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthbuddy.databinding.FragmentAnalysisExecBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class AnalysisExecFragment : Fragment() {

    private var _binding: FragmentAnalysisExecBinding? = null
    // !! --> Tell kotlin that it will never be null
    private val binding get() = _binding!!

    private val execCatList = ArrayList<String>()
    private val execTypeList = ArrayList<String>()
    private lateinit var execAnalysisAdapter: ExecAnalysisAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnalysisExecBinding.inflate(inflater, container, false)
        val view = binding.root

        val entries = ArrayList<Entry>()

        // Sample data points
        entries.add(Entry(1f, 10f))
        entries.add(Entry(2f, 20f))
        entries.add(Entry(3f, 15f))
        entries.add(Entry(4f, 30f))
        entries.add(Entry(5f, 25f))

        val dataSet = LineDataSet(entries, "Sample Data")
        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(dataSet)

        val lineData = LineData(dataSets)

        // Customize the X-axis
        val xAxis = binding.execGraph.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM // Position of the X-axis labels
        xAxis.setDrawGridLines(false) // Hide grid lines if not needed
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                // Implement your custom X-axis label formatting logic here
                // Return the formatted label as a String
                return "Day"
            }
        }

        // Customize the Y-axis (left)
        val yAxisLeft = binding.execGraph.axisLeft
        yAxisLeft.setDrawGridLines(false) // Hide grid lines if not needed
        yAxisLeft.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                // Implement your custom Y-axis label formatting logic here
                // Return the formatted label as a String
                return "Calories"
            }
        }

        // Customize the Y-axis (right)
        val yAxisRight = binding.execGraph.axisRight
        yAxisRight.isEnabled = false // Disable the right Y-axis

        binding.execGraph.data = lineData
        binding.execGraph.invalidate() // Refresh the chart

        // Recycler View
        execAnalysisAdapter = ExecAnalysisAdapter()
        binding.execAnalysisView.layoutManager = LinearLayoutManager(requireContext())
        binding.execAnalysisView.adapter = execAnalysisAdapter

        return view
    }

}