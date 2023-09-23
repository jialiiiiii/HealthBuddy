package com.example.healthbuddy.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.healthbuddy.R
import com.example.healthbuddy.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_settings,
            container,
            false
        )

        binding.imageView.setOnClickListener {
            activity?.onBackPressed()
        }

        // Set up theme spinner
        val spinnerTheme: Spinner = binding.spinnerTheme
        spinnerTheme.onItemSelectedListener = this

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.theme_selection,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTheme.adapter = adapter
        }

        // Set up language spinner
        val spinnerLanguage: Spinner = binding.spinnerLanguage
        spinnerLanguage.onItemSelectedListener = this

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.language_selection,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerLanguage.adapter = adapter
        }

        binding.textPrivacyPolicy.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_privacy)
        }

        binding.textTermsAndConditions.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_terms)
        }

        // Set the view's root from the binding object
        return binding.root
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (p0 == binding.spinnerTheme) {
            val selectedTheme = p0?.getItemAtPosition(p2).toString()

        } else if (p0 == binding.spinnerLanguage) {
            val selectedLanguage = p0?.getItemAtPosition(p2).toString()

        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}