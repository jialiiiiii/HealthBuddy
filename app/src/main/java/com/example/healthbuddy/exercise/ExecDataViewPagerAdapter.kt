package com.example.healthbuddy.exercise

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ExecDataViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AnalysisExecFragment()
            1 -> SuggestExecFragment()
            2 -> ExecCollectionFragment()
            else -> AnalysisExecFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}