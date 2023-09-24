package com.example.healthbuddy.com.example.healthbuddy

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.healthbuddy.R
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences("HealthBuddyPrefs", MODE_PRIVATE)

        // Hide default action bar
        supportActionBar?.hide()

        // Set theme
        val savedTheme = sharedPreferences.getInt("theme", 0)
        val nightMode = when (savedTheme) {
            0 -> AppCompatDelegate.MODE_NIGHT_NO
            1 -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)

        // Set language
        val savedLang = sharedPreferences.getString("language", "en")
        val locale = Locale(savedLang)
        val config = Configuration(resources.configuration)
        Locale.setDefault(locale)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Check if the user is logged in
        val loggedIn = sharedPreferences.getBoolean("loggedIn", false)

        // Inflate the appropriate layout based on the login state
        if (loggedIn) {
            setContentView(R.layout.main_activity)
        } else {
            setContentView(R.layout.landing_activity)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Reload or update the view
        recreate()
    }

}