package com.example.healthbuddy.exercise

import ExecDataFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.healthbuddy.R
import com.example.healthbuddy.addUserData.AddUserDataFragment
import com.example.healthbuddy.databinding.ActivityExecDataBinding
import com.google.android.material.tabs.TabLayout

class ExecData : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_exec_data)

        if (savedInstanceState == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment = ExecDataFragment()
            fragmentTransaction.add(R.id.fragment_container, fragment)
            fragmentTransaction.commit()
        }
    }
}