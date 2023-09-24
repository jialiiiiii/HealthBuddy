package com.example.healthbuddy.nutrition

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthbuddy.R
import com.example.healthbuddy.databinding.FragmentAnalysisNutriBinding
import com.example.healthbuddy.exercise.GraphMarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AnalysisNutriFragment : Fragment() {

    private var _binding: FragmentAnalysisNutriBinding? = null
    // !! --> Tell kotlin that it will never be null
    private val binding get() = _binding!!

    private val nutriCatList = ArrayList<String>()
    private val nutriTypeList = ArrayList<String>()
    private lateinit var nutriAnalysisAdapter: NutriAnalysisAdapter

    private lateinit var nutriDataViewModel: NutriDataViewModel

    private val entries = ArrayList<Entry>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnalysisNutriBinding.inflate(inflater, container, false)
        val view = binding.root

        // Get current month
        // Get the current date
        val currentDate = Calendar.getInstance().time

        // Format the date to get the current month in string format
        val sdf = SimpleDateFormat("MMMM", Locale.getDefault())
        val currentMonth = sdf.format(currentDate)

        // Initialize ViewModel
        nutriDataViewModel = ViewModelProvider(this).get(NutriDataViewModel::class.java)
        binding.nutriDataViewModel = nutriDataViewModel

        // Recycler View
        nutriAnalysisAdapter = NutriAnalysisAdapter(resources)

        // Edit
        nutriAnalysisAdapter.setOnActionDEditListener { nutriExecData ->
            val bundle = Bundle()
            bundle.putParcelable(UpdateNutriFragment.ARG_NUTRI_DATA, nutriExecData)

            val updateNutriFragment = UpdateNutriFragment()
            updateNutriFragment.arguments = bundle

            // Show the dialog fragment
            updateNutriFragment.show(requireActivity().supportFragmentManager, "UpdateNutriDialogFragment")
        }

        // Delete
        nutriAnalysisAdapter.setOnActionDeleteListener { userNutriDataToDelete ->
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle(R.string.del_confirmation)
            alertDialogBuilder.setMessage(R.string.del_confirmation_msg)
            alertDialogBuilder.setPositiveButton(R.string.del_confirm) { _, _ ->
                nutriDataViewModel.deleteNutriData(userNutriDataToDelete)
                Toast.makeText(requireContext(), R.string.del_exec_success, Toast.LENGTH_SHORT).show()
            }
            alertDialogBuilder.setNegativeButton(R.string.del_cancel) { dialog, _ ->
                dialog.dismiss() // Dismiss the dialog
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        // Set the adapter on your RecyclerView
        binding.nutriAnalysisView.layoutManager = LinearLayoutManager(requireContext())
        binding.nutriAnalysisView.adapter = nutriAnalysisAdapter

        // ViewModel
        // Outside conditional check: Let the compiler know that binding.nutriDataViewModel wont be null
        binding.nutriDataViewModel?.let { nutriDataViewModel ->
            nutriDataViewModel.getAllNutriData.observe(viewLifecycleOwner, Observer { userNutriData ->
                if (userNutriData.isNotEmpty()) {
                    nutriAnalysisAdapter.setData(userNutriData)
                    binding.noData.visibility = View.GONE
                    binding.month.text = "($currentMonth)"
                } else {
                    binding.noData.visibility = View.VISIBLE
                }
            })

            nutriDataViewModel.getTotalCaloriesGained.observe(viewLifecycleOwner, Observer { totalCaloriesBurntList ->
                // Clear the existing entries before adding new data
                entries.clear()

                // Map the totalCaloriesBurntList to Entry objects and add them to the entries list
                totalCaloriesBurntList.forEachIndexed { index, totalCaloriesGained ->
                    entries.add(Entry(totalCaloriesGained.day, totalCaloriesGained.totalCaloriesGained.toFloat()))
                }

                val label = context?.getString(R.string.cal_gained)
                val dataSet = LineDataSet(entries, label)
                val dataSets: ArrayList<ILineDataSet> = ArrayList()
                dataSets.add(dataSet)

                val lineData = LineData(dataSets)

                // Set Description
                val desc = context?.getString(R.string.graph_desc)
                binding.nutriGraph.description.text = desc

                // Set marker view
                val dayText = getString(R.string.day)
                val calGainedText = getString(R.string.cal_gained)
                val markerView = GraphMarkerView(requireContext(), R.layout.graph_marker_view, dayText, calGainedText)
                binding.nutriGraph.marker = markerView

                // Customize the X-axis
                val xAxis = binding.nutriGraph.xAxis
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
                val yAxisLeft = binding.nutriGraph.axisLeft
                yAxisLeft.setDrawGridLines(false) // Hide grid lines if not needed

                // Customize the Y-axis (right)
                val yAxisRight = binding.nutriGraph.axisRight
                yAxisRight.isEnabled = false // Disable the right Y-axis

                binding.nutriGraph.data = lineData
                binding.nutriGraph.invalidate() // Refresh the chart
            })
        }
        return view
    }
}