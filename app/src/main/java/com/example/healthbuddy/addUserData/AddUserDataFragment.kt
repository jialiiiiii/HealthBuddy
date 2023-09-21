package com.example.healthbuddy.addUserData

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.healthbuddy.AddUserDataViewPagerAdapter
import com.example.healthbuddy.databinding.FragmentAddUserDataBinding
import com.google.android.material.tabs.TabLayout
import com.example.healthbuddy.R

class AddUserDataFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var postViewPagerAdapter: AddUserDataViewPagerAdapter
    private lateinit var binding: FragmentAddUserDataBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddUserDataBinding.inflate(inflater, container, false)
        val view = binding.root

        val top = binding.layoutTop
        val bottom = binding.layoutBottom
        top.lineHeader.setBackgroundColor(Color.TRANSPARENT)

        // Initialize the ViewPager adapter
        postViewPagerAdapter = AddUserDataViewPagerAdapter(requireActivity())

        // Bind the TabLayout and ViewPager
        tabLayout = binding.postLayout
        viewPager2 = binding.execFragmentPager // Use R.id.exec_fragment_pager

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

        return view
    }
}