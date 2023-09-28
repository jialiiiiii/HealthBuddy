package com.example.healthbuddy.post

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.healthbuddy.R
import com.example.healthbuddy.database.Post
import com.example.healthbuddy.databinding.FragmentPostBinding
import com.example.healthbuddy.others.RecyclerViewItemDecoration
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var db: DatabaseReference
    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var nodeList: ArrayList<TempData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class
        binding = FragmentPostBinding.inflate(inflater, container, false)

        // Initialize shared preferences
        sharedPreferences = requireContext().getSharedPreferences("HealthBuddyPrefs", AppCompatActivity.MODE_PRIVATE)

        binding.postList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // Add item decoration for spacing
        val itemDecoration = RecyclerViewItemDecoration(15, 2)
        binding.postList.addItemDecoration(itemDecoration)

        binding.postList.hasFixedSize()
        postArrayList = arrayListOf<Post>()
        nodeList = arrayListOf<TempData>()
        getItemData()

        return binding.root
    }

    private fun getItemData() {
        db = FirebaseDatabase.getInstance().getReference("Posts")
        val userId = sharedPreferences.getString("userId", "")
        val query = db.orderByChild("postUser").equalTo(userId)

        if (userId?.isNotBlank() == true) {
            // Construct a query to retrieve posts with matching user ID
            val query = db.orderByChild("postUser").equalTo(userId)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var ky: String = ""
                        var pots: String = ""

                        for (postsnapshot in snapshot.children) {
                            val post = postsnapshot.getValue(Post::class.java)
                            postArrayList.add(post!!)
                            ky = postsnapshot.key.toString()
                            pots = post.postTitle.toString()
                            val tmppost = TempData(ky, pots)
                            nodeList.add(tmppost)
                        }

                        var adapter = PostAdapter(postArrayList)
                        binding.postList.adapter = adapter
                        adapter.setOnItemClickListener(object : PostAdapter.OnItemClickListener {
                            override fun onItemClick(position: Int) {
                                val ctpost = nodeList[position]
                                val nodePath = ctpost.id.toString()
                                val bundle = Bundle()
                                bundle.putString("post_id", nodePath.toString())

                                // Navigate to the edit post fragment with the bundle
                                findNavController().navigate(R.id.action_post_to_edit_post, bundle)
                            }
                        })

                        // Hide the progress bar when data retrieval is complete
                        binding.progressBar.visibility = View.GONE
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error here if necessary
                }
            })
        } else {
            // Handle the case where userId is blank or not found in SharedPreferences
        }
    }

}