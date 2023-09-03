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
import com.example.healthbuddy.databinding.ForumFragmentBinding

class ForumFragment : Fragment() {

    companion object {
        fun newInstance() = ForumFragment()
    }

    private lateinit var viewModel: ForumViewModel
    private lateinit var binding: ForumFragmentBinding

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

        // Access the layout
        val layoutFragment = binding.root.findViewById<View>(R.id.layoutFragment)

        // Change icon and text color
        val imageView = layoutFragment.findViewById<ImageView>(R.id.icon_account)
        imageView.setImageResource(R.drawable.ic_account_filled)
        val textView = layoutFragment.findViewById<TextView>(R.id.text_account)
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_green))



        // Set the view's root from the binding object
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ForumViewModel::class.java)
        // TODO: Use the ViewModel
    }

}