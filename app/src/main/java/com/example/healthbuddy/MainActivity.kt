package com.example.healthbuddy.com.example.healthbuddy

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.healthbuddy.R

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        // Check if the user is logged in
        sharedPreferences = getSharedPreferences("HealthBuddyPrefs", MODE_PRIVATE)
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