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
import com.example.healthbuddy.R
import com.example.healthbuddy.databinding.AccountFragmentBinding

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

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Profile"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Post"))

        // Set the view's root from the binding object
        return binding.root
    }

}