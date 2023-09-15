package com.example.healthbuddy.landing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.healthbuddy.R

class SecondFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.landing_second_fragment, container, false)

        // Find the Button by its ID
        val button = view.findViewById<Button>(R.id.button)

        // Set an OnClickListener for the Button
        button.setOnClickListener {
            findNavController().navigate(R.id.action_second_to_third)
        }

        return view
    }

}