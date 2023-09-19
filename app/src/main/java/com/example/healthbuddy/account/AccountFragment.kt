package com.example.healthbuddy.com.example.healthbuddy.account

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.healthbuddy.R
import com.example.healthbuddy.databinding.AccountFragmentBinding
import com.google.android.material.tabs.TabLayout

class AccountFragment : Fragment() {

    private lateinit var viewModel: AccountViewModel
    private lateinit var binding: AccountFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.account_fragment,
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
        tabLayout.addTab(binding.tabLayout.newTab().setText("Profile"))
        tabLayout.addTab(binding.tabLayout.newTab().setText("Post"))

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