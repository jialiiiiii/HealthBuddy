package com.example.healthbuddy.settings

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.healthbuddy.R
import com.example.healthbuddy.databinding.FragmentSettingsBinding
import java.util.Locale

class SettingsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences

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

        // Initialize SharedPreferences
        sharedPreferences =
            requireContext().getSharedPreferences("HealthBuddyPrefs", Context.MODE_PRIVATE)

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

            val position = sharedPreferences.getInt("theme", 0)
            spinnerTheme.setSelection(position)
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

            val position = getPosition(sharedPreferences.getString("language", "en")!!)
            spinnerLanguage.setSelection(position)
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
            // Set theme
            val savedTheme = sharedPreferences.getInt("theme", 0)
            val selectedTheme = p2

            if (savedTheme != p2) {
                sharedPreferences.edit().putInt("theme", selectedTheme).apply()

                val nightMode = when (selectedTheme) {
                    0 -> AppCompatDelegate.MODE_NIGHT_NO
                    1 -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_NO
                }

                AppCompatDelegate.setDefaultNightMode(nightMode)

                // Refresh the UI
                activity?.recreate()
            }

        } else if (p0 == binding.spinnerLanguage) {
            // Set language
            val savedLanguage = sharedPreferences.getString("language", "en")
            val selectedLanguage = getLanguage(p2)

            if (savedLanguage != selectedLanguage) {
                sharedPreferences.edit().putString("language", selectedLanguage).apply()

                val locale = Locale(selectedLanguage)
                val config = Configuration(resources.configuration)
                Locale.setDefault(locale)
                config.setLocale(locale)
                resources.updateConfiguration(config, resources.displayMetrics)

                // Refresh the UI
                activity?.recreate()
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    private fun getLanguage(position: Int): String {
        return when (position) {
            0 -> "en"
            1 -> "zh"
            2 -> "ms"
            else -> "en"
        }
    }

    private fun getPosition(language: String): Int{
        return when (language) {
            "en" -> 0
            "zh" -> 1
            "ms" -> 2
            else -> 0
        }
    }
}