package com.example.healthbuddy

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
import com.example.healthbuddy.databinding.AccountFragmentBinding

class AccountFragment : Fragment() {

    private lateinit var viewModel: AccountViewModel
    private lateinit var binding: AccountFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.account_fragment,
            container,
            false
        )

        // Access the layout
        val layout = binding.root.findViewById<View>(R.id.layoutBottom)

        // Change icon and text color
        val thisIcon = layout.findViewById<ImageView>(R.id.icon_account)
        thisIcon.setImageResource(R.drawable.ic_account_filled)
        val thisText = layout.findViewById<TextView>(R.id.text_account)
        thisText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_green))

        // Set the view's root from the binding object
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
    }

}