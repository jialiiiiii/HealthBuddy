package com.example.healthbuddy.exercise

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthbuddy.R
import com.example.healthbuddy.databinding.FragmentAnalysisExecBinding
import com.example.healthbuddy.exercise.UpdateExecFragment.Companion.ARG_EXEC_DATA
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AnalysisExecFragment : Fragment() {

    private var _binding: FragmentAnalysisExecBinding? = null
    // !! --> Tell kotlin that it will never be null
    private val binding get() = _binding!!

    private val execCatList = ArrayList<String>()
    private val execTypeList = ArrayList<String>()
    private lateinit var execAnalysisAdapter: ExecAnalysisAdapter

    private lateinit var execDataViewModel: ExecDataViewModel

    private val entries = ArrayList<Entry>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnalysisExecBinding.inflate(inflater, container, false)
        val view = binding.root

        // Get current month
        // Get the current date
        val currentDate = Calendar.getInstance().time

        // Format the date to get the current month in string format
        val sdf = SimpleDateFormat("MMMM", Locale.getDefault())
        val currentMonth = sdf.format(currentDate)

        // Initialize ViewModel
        execDataViewModel = ViewModelProvider(this).get(ExecDataViewModel::class.java)
        binding.execDataViewModel = execDataViewModel

        // Recycler View
        execAnalysisAdapter = ExecAnalysisAdapter()

        // Edit
        execAnalysisAdapter.setOnActionDEditListener { userExecData ->
            val bundle = Bundle()
            bundle.putParcelable(ARG_EXEC_DATA, userExecData)

            val updateExecFragment = UpdateExecFragment()
            updateExecFragment.arguments = bundle

            // Show the dialog fragment
            updateExecFragment.show(requireActivity().supportFragmentManager, "UpdateExecDialogFragment")
        }

        // Delete
        execAnalysisAdapter.setOnActionDeleteListener { userExecDataToDelete ->
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle(R.string.del_confirmation)
            alertDialogBuilder.setMessage(R.string.del_confirmation_msg)
            alertDialogBuilder.setPositiveButton(R.string.del_confirm) { _, _ ->
                // Assuming userExecDataToDelete is the item to be deleted
                execDataViewModel.deleteExecData(userExecDataToDelete)
                Toast.makeText(requireContext(), R.string.del_exec_success, Toast.LENGTH_SHORT).show()
            }
            alertDialogBuilder.setNegativeButton(R.string.del_cancel) { dialog, _ ->
                dialog.dismiss() // Dismiss the dialog
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        // Set the adapter on your RecyclerView
        binding.execAnalysisView.layoutManager = LinearLayoutManager(requireContext())
        binding.execAnalysisView.adapter = execAnalysisAdapter

        // ViewModel
        // Outside conditional check: Let the compiler know that binding.execDataViewModel wont be null
        binding.execDataViewModel?.let { execDataViewModel ->
            execDataViewModel.getAllExecData.observe(viewLifecycleOwner, Observer { userExecData ->
                if (userExecData.isNotEmpty()) {
                    execAnalysisAdapter.setData(userExecData)
                    binding.noData.visibility = View.GONE
                    binding.month.text = "($currentMonth)"
                } else {
                    binding.noData.visibility = View.VISIBLE
                }
            })

            execDataViewModel.getTotalCaloriesBurnt.observe(viewLifecycleOwner, Observer { totalCaloriesBurntList ->
                // Clear the existing entries before adding new data
                entries.clear()

                // Map the totalCaloriesBurntList to Entry objects and add them to the entries list
                totalCaloriesBurntList.forEachIndexed { index, totalCaloriesBurnt ->
                    entries.add(Entry(totalCaloriesBurnt.day, totalCaloriesBurnt.totalCaloriesBurnt.toFloat()))
                }

                val dataSet = LineDataSet(entries, R.string.cal_burnt.toString())
                val dataSets: ArrayList<ILineDataSet> = ArrayList()
                dataSets.add(dataSet)

                val lineData = LineData(dataSets)

                // Set Description
                binding.execGraph.description.text = R.string.graph_desc.toString()

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