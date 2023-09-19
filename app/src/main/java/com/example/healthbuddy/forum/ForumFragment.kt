package com.example.healthbuddy.com.example.healthbuddy.forum

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.healthbuddy.R
import com.example.healthbuddy.databinding.ForumFragmentBinding

class ForumFragment : Fragment() {

    private lateinit var viewModel: ForumViewModel
    private lateinit var binding: ForumFragmentBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.forum_fragment,
            container,
            false
        )

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("HealthBuddyPrefs", Context.MODE_PRIVATE)

        val top = binding.layoutTop
        val bottom = binding.layoutBottom

        // Change icon and text color
        bottom.iconForum.setImageResource(R.drawable.ic_forum_filled)
        bottom.textForum.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_green))

        // Make cards clickable
        top.iconSettings.setOnClickListener {

        }
        bottom.cardNutrition.setOnClickListener {

        }
        bottom.cardAdd.setOnClickListener {

        }
        bottom.cardExercise.setOnClickListener {

        }
        bottom.cardAccount.setOnClickListener {
            findNavController().navigate(R.id.action_forum_to_account)
        }

        // Display login message once
        val loginMsg = sharedPreferences.getString("loginMsg", "") ?: ""

        Log.i("Testing123", "Hello, $loginMsg")

        if (loginMsg.isNotEmpty()) {
            Toast.makeText(context, loginMsg, Toast.LENGTH_SHORT).show()
        }

        val editor = sharedPreferences.edit()
        editor.remove("loginMsg")
        editor.apply()

        // Set the view's root from the binding object
        return binding.root
    }

}