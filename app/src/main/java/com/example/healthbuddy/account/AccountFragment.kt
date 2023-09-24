package com.example.healthbuddy.com.example.healthbuddy.account

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.healthbuddy.R
import com.example.healthbuddy.databinding.FragmentAccountBinding
import com.google.android.material.tabs.TabLayout

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_account,
            container,
            false
        )

        val top = binding.layoutTop
        val bottom = binding.layoutBottom

        // Change icon and text color
        bottom.iconAccount.setImageResource(R.drawable.ic_account_filled)
        bottom.textAccount.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_green))
        top.lineHeader.setBackgroundColor(Color.TRANSPARENT)

        // Make cards clickable
        top.iconSettings.setOnClickListener {
            findNavController().navigate(R.id.action_account_to_settings)
        }
        bottom.cardForum.setOnClickListener {
            findNavController().navigate(R.id.action_account_to_forum)
        }
        bottom.cardNutrition.setOnClickListener {

        }
        bottom.cardAdd.setOnClickListener {

        }
        bottom.cardExercise.setOnClickListener {

        }

        // Set up tab layouts
        var tabLayout = binding.tabLayout
        var viewPager2 = binding.viewPager
        var viewPagerAdapter = ViewPagerAdapter(requireActivity())

        // Add tab items
        tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.profile))
        tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.post))

        // Set up viewpager
        viewPager2.setAdapter(viewPagerAdapter)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager2.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)!!.select()
            }
        })

        // Set the view's root from the binding object
        return binding.root
    }

}