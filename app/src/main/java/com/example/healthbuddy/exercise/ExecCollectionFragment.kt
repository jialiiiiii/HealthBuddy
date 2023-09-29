package com.example.healthbuddy.exercise

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.healthbuddy.R
import com.example.healthbuddy.database.Collection
import com.example.healthbuddy.database.Post
import com.example.healthbuddy.databinding.FragmentExecCollectionBinding
import com.example.healthbuddy.post.PostAdapter
import com.example.healthbuddy.others.RecyclerViewItemDecoration
import com.example.healthbuddy.post.TempData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ExecCollectionFragment : Fragment() {

    private lateinit var binding: FragmentExecCollectionBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var db: DatabaseReference

    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var nodeList: ArrayList<TempData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_exec_collection,
            container,
            false)

        // Initialize SharedPreferences
        sharedPreferences =
            requireContext().getSharedPreferences("HealthBuddyPrefs", Context.MODE_PRIVATE)

        binding.postList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

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
        db = FirebaseDatabase.getInstance().getReference("Collections")
        val userId = sharedPreferences.getString("userId", "") ?: ""

        db.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val collection = dataSnapshot.getValue(Collection::class.java)
                    val postIds: ArrayList<String>? = collection?.postId

                    // Check if the user has any posts in their collection
                    if (!postIds.isNullOrEmpty()) {

                        // Get one post
                        for (postId in postIds) {
                            val query = FirebaseDatabase.getInstance().getReference("Posts").child(postId)

                            query.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        var ky: String = ""
                                        var pots: String = ""

                                        val post = snapshot.getValue(Post::class.java)

                                        if (post != null && post.postTag == "Exercise") {
                                            postArrayList.add(post)
                                            ky = snapshot.key.toString()
                                            pots = post.postTitle.toString()
                                            val tmppost = TempData(ky, pots)
                                            nodeList.add(tmppost)

                                            binding.msg.text = ""
                                        }

                                        var adapter = PostAdapter(postArrayList)
                                        binding.postList.adapter = adapter
                                        adapter.setOnItemClickListener(object : PostAdapter.OnItemClickListener {
                                            override fun onItemClick(position: Int) {
                                                val ctpost = nodeList[position]
                                                val nodePath = ctpost.id.toString()

                                                val bundle = Bundle()
                                                bundle.putString("post_id", nodePath)

                                                findNavController().navigate(R.id.action_exercise_collection_to_forum_details, bundle)
                                            }
                                        })
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })
                        }
                    } else {

                    }
                } else {

                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })

    }
}