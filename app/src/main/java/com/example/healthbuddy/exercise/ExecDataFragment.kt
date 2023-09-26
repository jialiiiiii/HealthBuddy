import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.healthbuddy.R
import com.example.healthbuddy.databinding.FragmentExecDataBinding
import com.example.healthbuddy.exercise.ExecDataViewPagerAdapter
import com.google.android.material.tabs.TabLayout

class ExecDataFragment : Fragment() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    lateinit var postViewPagerAdapter: ExecDataViewPagerAdapter

    private lateinit var binding: FragmentExecDataBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize the View Binding
        binding = FragmentExecDataBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize the ViewPager adapter
        postViewPagerAdapter = ExecDataViewPagerAdapter(requireActivity())

        val top = binding.layoutTop
        val bottom = binding.layoutBottom

        // Change icon and text color
        bottom.iconExercise.setImageResource(R.drawable.ic_exercise_filled)
        bottom.textExercise.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_green))
        top.lineHeader.setBackgroundColor(Color.TRANSPARENT)

        // Make cards clickable
        top.iconSettings.setOnClickListener {
            findNavController().navigate(R.id.action_exercise_to_settings)
        }
        bottom.cardForum.setOnClickListener {
            findNavController().navigate(R.id.action_exercise_to_forum)
        }
        bottom.cardNutrition.setOnClickListener {
            findNavController().navigate(R.id.action_exercise_to_nutrition)
        }
        bottom.cardAdd.setOnClickListener {
            findNavController().navigate(R.id.action_exercise_to_add)
        }
        bottom.cardAccount.setOnClickListener {
            findNavController().navigate(R.id.action_exercise_to_account)
        }

        // Bind the TabLayout and ViewPager
        tabLayout = binding.postLayout
        viewPager2 = binding.viewPager

        // Set the adapter for the ViewPager
        viewPager2.adapter = postViewPagerAdapter

        // Add an OnTabSelectedListener to the TabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager2.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Optional: Add any behavior for when a tab is unselected
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Optional: Add any behavior for when a tab is reselected
            }
        })

        // Register an OnPageChangeCallback for the ViewPager2
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)?.select()
            }
        })

        return view
    }
}