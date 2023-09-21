package com.example.healthbuddy.nutrition

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
import androidx.lifecycle.lifecycleScope
import com.example.healthbuddy.R
import com.example.healthbuddy.database.UserNutritionDao
import com.example.healthbuddy.database.UserNutritionData
import com.example.healthbuddy.database.UserNutritionDatabase
import com.example.healthbuddy.databinding.FragmentAddNutriBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


// , AdapterView.OnItemSelectedListener
class AddNutriFragment : Fragment(), AdapterView.OnItemSelectedListener {
    // hold the View Binding instance for your fragment
    private var _binding: FragmentAddNutriBinding? = null
    // custom getter to access the View Binding instance
    private val binding get() = _binding!!

    private var foodTypeSelectionPosition: Int = 0

    private lateinit var userNutritionDao: UserNutritionDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Replace the layout inflating code with ViewBinding
        _binding = FragmentAddNutriBinding.inflate(inflater, container, false)
        val view = binding.root

        // Set up spinner
        // Category
        binding.foodCategorySpinner.onItemSelectedListener = this

        ArrayAdapter.createFromResource(requireContext(), R.array.food_group, android.R.layout.simple_spinner_item).also{
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.foodCategorySpinner.adapter = adapter
        }

        // Duration seek bar
        binding.sizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update the TextView with the current progress value
                binding.sizeValue.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // This method is called when the user starts dragging the SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // This method is called when the user stops dragging the SeekBar
            }
        })

        // Set up serving size
        ArrayAdapter.createFromResource(requireContext(), R.array.times_value, android.R.layout.simple_spinner_item).also{
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.servingSizeSpinner.adapter = adapter
        }

        // Initialize Calendar instance
        val calendar = Calendar.getInstance()

        // Set initial text for date and time buttons using ViewBinding
        updateDateButtonText(calendar)
        updateTimeButtonText(calendar)

        // Set click listeners for date and time buttons using ViewBinding
        binding.datePickerButton.setOnClickListener { showDatePicker(calendar) }
        binding.timePickerButton.setOnClickListener { showTimePicker(calendar) }

        binding.sizeSeekBar.min = 5
        binding.sizeSeekBar.max = 500

        // Set onClick Listener for button
        binding.saveButton.setOnClickListener{
            viewLifecycleOwner.lifecycleScope.launch {
                saveUserNutriData()
            }
        }

        // Restore the selected position of foodTypeSpinner if it exists
        if (savedInstanceState != null) {
            foodTypeSelectionPosition = savedInstanceState.getInt("foodTypeSelectionPosition", 0)
            binding.foodTypeSpinner.setSelection(foodTypeSelectionPosition)
        }

        return view
    }

    private fun getCalories(foodCat: Int, foodType: Int): Double{
        var cal: Double = 0.0

        when(foodCat){
            0 -> {
                when(foodType){
                    0 -> cal = 0.52
                    1 -> cal = 0.89
                    2 -> cal = 0.43
                    3 -> cal = 0.39
                    4 -> cal = 0.57
                    5 -> cal = 0.69
                    6 -> cal = 0.30
                    7 -> cal = 0.32
                    8 -> cal = 0.50
                    9 -> cal = 0.60
                }
            }
            1 -> {
                when(foodType){
                    0 -> cal = 0.34
                    1 -> cal = 0.23
                    2 -> cal = 0.25
                    3 -> cal = 0.77
                    4 -> cal = 0.18
                    5 -> cal = 0.41
                    6 -> cal = 0.22
                    7 -> cal = 0.16
                    8 -> cal = 0.20
                    9 -> cal = 0.26
                }
            }
            2 -> {
                when(foodType){
                    0 -> cal = 2.4
                    1 -> cal = 3.8
                    2 -> cal = 3.5
                    3 -> cal = 3.7
                    4 -> cal = 3.8
                    5 -> cal = 3.5
                }
            }
            3 -> {
                when(foodType){
                    0 -> cal = 2.6
                    1 -> cal = 1.2
                    2 -> cal = 0.99
                    3 -> cal = 0.84
                    4 -> cal = 2.8
                    5 -> cal = 2.9
                    6 -> cal = 1.44
                    7 -> cal = 5.9
                    8 -> cal = 0.63
                }
            }
            4 -> {
                when(foodType){
                    0 -> cal = 0.49
                    1 -> cal = 0.54
                    2 -> cal = 3.4
                    3 -> cal = 7.2
                    4 -> cal = 3.5
                    5 -> cal = 2.4
                    6 -> cal = 2.0
                }
            }
        }

        return cal
    }

    private suspend fun saveUserNutriData(){
        var calObtained: Double = 0.0
        var calObtainedHolder: String = ""

        // Database
        val db = UserNutritionDatabase.getInstance(requireContext())

        userNutritionDao = db.userNutritionDao

        // Formula
        // Total Calories Obtained = Calories per gram * size * serving

        calObtained = getCalories(binding.foodCategorySpinner.selectedItemPosition, binding.foodTypeSpinner.selectedItemPosition)
        calObtained *= binding.sizeSeekBar.progress.toString().toDouble()
        calObtained *= binding.servingSizeSpinner.selectedItem.toString().toDouble()

//        calObtainedHolder = String.format("%.2f", calObtained)
//        calObtained = calObtainedHolder.toDouble()

        val userNutritionData = UserNutritionData(
            food_category = binding.foodCategorySpinner.selectedItem.toString(),
            food_type = binding.foodTypeSpinner.selectedItem.toString(),
            food_size = binding.sizeSeekBar.progress.toString().toInt(),
            food_serving_size = binding.servingSizeSpinner.selectedItem.toString().toInt(),
            intake_date = binding.datePickerButton.text.toString(),
            intake_time = binding.timePickerButton.text.toString(),
            cal_obtained = calObtained)

        db.userNutritionDao.insert(userNutritionData)

        Toast.makeText(requireContext(), "Nutrition Data successfully saved.", Toast.LENGTH_SHORT).show()
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var foodTypeArr: Int = 0
        when(position){
            0 -> foodTypeArr = R.array.fruits_selection
            1 -> foodTypeArr = R.array.vegetables_selection
            2 -> foodTypeArr = R.array.grains_selection
            3 -> foodTypeArr = R.array.protein_foods_selection
            4 -> foodTypeArr = R.array.grains_selection
        }

        // Store the selected position in foodTypeSpinner
        foodTypeSelectionPosition = binding.foodTypeSpinner.selectedItemPosition

        ArrayAdapter.createFromResource(requireContext(), foodTypeArr, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.foodTypeSpinner.adapter = adapter
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // Ignore
        var foodTypeArr: Int = 0
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the selected position of foodTypeSpinner
        outState.putInt("foodTypeSelectionPosition", foodTypeSelectionPosition)
    }
}