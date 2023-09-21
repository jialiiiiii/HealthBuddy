package com.example.healthbuddy.nutrition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.healthbuddy.databinding.FragmentAnalysisNutriBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AnalysisNutriFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AnalysisNutriFragment : Fragment() {
    private var _binding: FragmentAnalysisNutriBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnalysisNutriBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }
}