package com.example.healthbuddy.com.example.healthbuddy.account

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.healthbuddy.post.PostFragment
import com.example.healthbuddy.account.ProfileFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProfileFragment()
            1 -> PostFragment()
            else -> ProfileFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}