package com.example.healthbuddy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.healthbuddy.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.landing_activity)
    }

}