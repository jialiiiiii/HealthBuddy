package com.example.healthbuddy.nutrition

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class NutriDataViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AnalysisNutriFragment()
            1 -> SuggestMealFragment()
            2 -> AnalysisNutriFragment()
            else -> AnalysisNutriFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}