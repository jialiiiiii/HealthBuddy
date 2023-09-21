package com.example.healthbuddy

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.healthbuddy.exercise.AddExecFragment
import com.example.healthbuddy.nutrition.AddNutriFragment
import com.example.healthbuddy.post.PostFragment

class AddUserDataViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PostFragment()
            1 -> AddNutriFragment()
            2 -> AddExecFragment()
            else -> AddNutriFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}