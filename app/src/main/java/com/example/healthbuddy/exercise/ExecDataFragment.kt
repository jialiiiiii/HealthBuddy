import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
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
        top.lineHeader.setBackgroundColor(Color.TRANSPARENT)

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