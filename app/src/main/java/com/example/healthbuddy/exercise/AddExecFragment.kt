package com.example.healthbuddy.exercise

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.healthbuddy.R
import com.example.healthbuddy.database.UserExecData
import com.example.healthbuddy.databinding.FragmentAddExecBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddExecFragment : Fragment(), AdapterView.OnItemSelectedListener {
    // hold the View Binding instance for your fragment
    private var _binding: FragmentAddExecBinding? = null
    // custom getter to access the View Binding instance
    private val binding get() = _binding!!

    private var maxValue: Int = 0
    private var minValue: Int = 0

    private var execTypeSelectionPosition: Int = 0

    private lateinit var execDataViewModel: ExecDataViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Replace the layout inflating code with ViewBinding
        _binding = FragmentAddExecBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize ViewModel
        execDataViewModel = ViewModelProvider(this).get(ExecDataViewModel::class.java)
        binding.execDataViewModel = execDataViewModel

        // Spinner
        // Set up exercise category
        binding.exerciseCategorySpinner.onItemSelectedListener = this

        ArrayAdapter.createFromResource(requireContext(), R.array.exer_group, R.layout.spinner_item).also{
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.exerciseCategorySpinner.adapter = adapter
        }

        binding.durationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update the TextView with the current progress value
                binding.durationValue.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // This method is called when the user starts dragging the SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // This method is called when the user stops dragging the SeekBar
            }
        })

        // Set up set(s) done
        ArrayAdapter.createFromResource(requireContext(), R.array.times_value, R.layout.spinner_item).also{
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.exerciseSetSpinner.adapter = adapter
        }

        // Initialize Calendar instance
        val calendar = Calendar.getInstance()

        // Set initial text for date and time buttons using ViewBinding
        updateDateButtonText(calendar)
        updateTimeButtonText(calendar)

        // Set click listeners for date and time buttons using ViewBinding
        binding.datePickerButton.setOnClickListener { showDatePicker(calendar) }
        binding.timePickerButton.setOnClickListener { showTimePicker(calendar) }

        // Set onClick Listener for button
        binding.saveButton.setOnClickListener{
            saveUserExecData()
        }

        // Restore the selected position of foodTypeSpinner if it exists
        if (savedInstanceState != null) {
            execTypeSelectionPosition = savedInstanceState.getInt("execTypeSelectionPosition", 0)
            binding.exerciseTypeSpinner.setSelection(execTypeSelectionPosition)
        }

        return view
    }

    private fun getMET(execCat: Int, execType: Int): Double{
        var met: Double = 0.0

        when(execCat){
            0 -> {
                when(execType){
                    0 -> met = 7.0
                    1 -> met = 6.0
                    2 -> met = 8.5
                    3 -> met = 12.0
                    4 -> met = 9.0
                    5 -> met = 6.75
                    6 -> met = 5.5
                    7 -> met = 7.0
                    8 -> met = 7.5
                }
            }
            1 -> {
                when(execType){
                    0 -> met = 1.5
                    1 -> met = 2.5
                    2 -> met = 2.5
                    3 -> met = 2.5
                    4 -> met = 2.5
                    5 -> met = 2.0
                    6-> met = 2.0
                }
            }
            2 ->  met = 3.5
            3 ->  met = 2.5
        }

        return met
    }

    private fun saveUserExecData(){
        var weight: Double = 65.0
        var calBurnt: Double = 0.0
        var calBurntHolder: String = ""

        // Formula
        // Total Calories Burned (per minute) = (3.5 * MET * Weight in kilograms) / 200
        // Calories Burned per Repetition     = (3.5 * MET * Weight in kilograms) / (200 * Repetitions)

        var met: Double = getMET(binding.exerciseCategorySpinner.selectedItemPosition, binding.exerciseTypeSpinner.selectedItemPosition)

        when(binding.exerciseCategorySpinner.selectedItemPosition){
            // Calculated in Mins
            0, 1 -> {
                calBurnt = (3.5 * met * weight) / 200.0
                calBurnt *= binding.durationSeekBar.progress.toString().toDouble()
                calBurnt *= binding.exerciseSetSpinner.selectedItem.toString().toDouble()
            }
            else -> {
                calBurnt = (3.5 * met * weight) / (200.0 * binding.durationSeekBar.progress.toString().toDouble())
                calBurnt *= binding.exerciseSetSpinner.selectedItem.toString().toDouble()
            }
        }

        calBurntHolder = String.format("%.2f", calBurnt)
        calBurnt = calBurntHolder.toDouble()

        val userExecData = UserExecData(
            exec_category = binding.exerciseCategorySpinner.selectedItemPosition,
            exec_type = binding.exerciseTypeSpinner.selectedItemPosition,
            exec_duration_rep = binding.durationSeekBar.progress.toString().toInt(),
            exec_set = binding.exerciseSetSpinner.selectedItem.toString().toInt(),
            exec_date = binding.datePickerButton.text.toString(),
            exec_time = binding.timePickerButton.text.toString(),
            cal_burnt = calBurnt)

        execDataViewModel.insertExecData(userExecData)

        Toast.makeText(requireContext(), R.string.add_exec_success, Toast.LENGTH_SHORT).show()
    }

    private fun showDatePicker(calendar: Calendar) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.CustomPickerDialog,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateButtonText(calendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Set the text color for the buttons using a color resource
        datePickerDialog.setOnShowListener { dialog ->
            val negativeButton = (dialog as DatePickerDialog).getButton(DialogInterface.BUTTON_NEGATIVE)
            val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)

            // Use a color resource for the text color
            negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.title))
            positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.title))
        }

        datePickerDialog.show()
    }

    private fun showTimePicker(calendar: Calendar) {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            R.style.CustomPickerDialog, // Apply your custom dialog style here
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                updateTimeButtonText(calendar)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // Set to true for 24-hour format
        )

        // Set the text color for the buttons using a color resource
        timePickerDialog.setOnShowListener { dialog ->
            val negativeButton = (dialog as TimePickerDialog).getButton(DialogInterface.BUTTON_NEGATIVE)
            val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)

            // Use a color resource for the text color
            negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.title))
            positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.title))
        }

        timePickerDialog.show()
    }

    private fun updateDateButtonText(calendar: Calendar) {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        binding.datePickerButton.text = dateFormat.format(calendar.time)
    }

    private fun updateTimeButtonText(calendar: Calendar) {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        binding.timePickerButton.text = timeFormat.format(calendar.time)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding instance
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, Id: Long) {
        var execTypeArr: Int = 0

        when(position){
            0 -> {      // Cardio
                execTypeArr = R.array.cardio_selection
                changeToDuration()
            }
            1 -> {      // Flexibility
                execTypeArr = R.array.flex_selection
                changeToDuration()
            }
            2 -> {      // Resistance
                execTypeArr = R.array.res_selection
                changeToRepetition()
            }
            3 -> {      // Neuromotor
                execTypeArr = R.array.neuro_selection
                changeToRepetition()
            }
        }

        binding.durationValue.text = binding.durationSeekBar.progress.toString()
        binding.durationSeekBar.min = maxValue
        binding.durationSeekBar.max = minValue

        ArrayAdapter.createFromResource(requireContext(), execTypeArr, R.layout.spinner_item).also{
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.exerciseTypeSpinner.adapter = adapter
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // Ignore
        var execTypeArr: Int = 0
    }

    private fun changeToRepetition(){
        binding.duration.text = getString(R.string.repetition_value)
        binding.mins.text = getString(R.string.repetition_set)
        binding.durationStart.text = getString(R.string.repetition_start)
        binding.durationEnd.text = getString(R.string.repetition_end)

        maxValue = 1
        minValue = 50
    }

    private fun changeToDuration(){
        binding.duration.text = getString(R.string.duration)
        binding.mins.text = getString(R.string.duration_mins)
        binding.durationStart.text = getString(R.string.duration_start)
        binding.durationEnd.text = getString(R.string.duration_end)

        maxValue = 5
        minValue = 60
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the selected position of foodTypeSpinner
        outState.putInt("execTypeSelectionPosition", execTypeSelectionPosition)
    }
}