package com.example.healthbuddy.account

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import com.example.healthbuddy.R
import com.example.healthbuddy.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: ProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class
        binding = ProfileFragmentBinding.inflate(inflater, container, false)

        // Set up gender spinner
        val spinner : Spinner = binding.spinnerGender
        spinner.onItemSelectedListener = this

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gender_selection,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }


        // Set the view's root from the binding object
        return binding.root
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}