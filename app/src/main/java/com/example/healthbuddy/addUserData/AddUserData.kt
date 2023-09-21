package com.example.healthbuddy.addUserData

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.healthbuddy.AddUserDataViewPagerAdapter
import com.example.healthbuddy.databinding.ActivityAddUserDataBinding
import com.google.android.material.tabs.TabLayout

class AddUserData : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    lateinit var postViewPagerAdapter: AddUserDataViewPagerAdapter

    private lateinit var binding: ActivityAddUserDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        // Initialize the View Binding
        binding = ActivityAddUserDataBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val top = binding.layoutTop
        val bottom = binding.layoutBottom

        top.lineHeader.setBackgroundColor(Color.TRANSPARENT)

        // Initialize the ViewPager adapter
        postViewPagerAdapter =
            AddUserDataViewPagerAdapter(this)

        // Bind the TabLayout and ViewPager
        tabLayout = binding.postLayout
        viewPager2 = binding.viewPager

        // Set the adapter for the ViewPager
        viewPager2.adapter = postViewPagerAdapter

        // Add an OnTabSelectedListener to the TabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager2.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Optional: Add any behavior for when a tab is unselected
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Optional: Add any behavior for when a tab is reselected
            }
        })

        // Register an OnPageChangeCallback for the ViewPager2
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)?.select()
            }
        })
    }
}