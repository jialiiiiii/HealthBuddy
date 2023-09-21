package com.example.healthbuddy.addUserData

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.healthbuddy.R

class AddUserData : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_user_data)

        if (savedInstanceState == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment = AddUserDataFragment()
            fragmentTransaction.add(R.id.fragment_container, fragment)
            fragmentTransaction.commit()
        }
    }
}