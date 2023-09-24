package com.example.healthbuddy.forum

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.healthbuddy.R
import com.example.healthbuddy.database.Post
import com.example.healthbuddy.databinding.FragmentCollectionBinding
import com.example.healthbuddy.post.PostAdapter
import com.example.healthbuddy.post.RecyclerViewItemDecoration
import com.example.healthbuddy.post.tempData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.healthbuddy.database.Collection

class CollectionFragment : Fragment() {

    private lateinit var binding: FragmentCollectionBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var db: DatabaseReference
    var sImage: String? = ""
    var nodeId = ""

    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var nodeList: ArrayList<tempData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_collection,
            container,
            false)

        // Initialize SharedPreferences
        sharedPreferences =
            requireContext().getSharedPreferences("HealthBuddyPrefs", Context.MODE_PRIVATE)

        val top = binding.layoutTop
        val bottom = binding.layoutBottom

        // Change icon and text color
        bottom.iconForum.setImageResource(R.drawable.ic_forum_filled)
        bottom.textForum.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_green))

        // Display login message once
        val loginMsg = sharedPreferences.getString("loginMsg", "") ?: ""

        if (loginMsg.isNotEmpty()) {
            Toast.makeText(context, loginMsg, Toast.LENGTH_SHORT).show()
        }

        val editor = sharedPreferences.edit()
        editor.remove("loginMsg")
        editor.apply()

        // Forum's code
        binding.postList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // Add item decoration for spacing
        val itemDecoration = RecyclerViewItemDecoration(15, 2)
        binding.postList.addItemDecoration(itemDecoration)
        binding.postList.hasFixedSize()
        postArrayList = arrayListOf<Post>()
        nodeList = arrayListOf<tempData>()
        getItemData()


        return binding.root
    }

    //    private fun getItemData() {
    //
    //        val uid = "5rQ0XILjLtT7B8VEsTVA3ScdLx82"
    //        val db: DatabaseReference = FirebaseDatabase.getInstance().getReference("Collections")
    //        var postIds: ArrayList<String>
    //
    //        // get post ids
    //        db.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
    //            override fun onDataChange(dataSnapshot: DataSnapshot) {
    //                var result = dataSnapshot.getValue(Collection::class.java)
    //
    //
    //                if (result?.postId != null &&result !=null) {
    //                    postIds = result.postId
    //
    //
    //
    //                    // get posts
    //                    val query = FirebaseDatabase.getInstance().getReference("Posts")
    //                        .orderByKey()
    //                        .startAt(postIds.first()) // Start query at the first postId
    //                        .endAt(postIds.last())   // End query at the last postId
    //
    //                    query.addValueEventListener(object : ValueEventListener {
    //                        override fun onDataChange(snapshot: DataSnapshot) {
    //                            Log.i("Testing456","query: $snapshot")
    //
    //                            if (snapshot.exists()) {
    //
    //                                postArrayList.clear() // Clear the previous data
    //                                var ky: String = ""
    //                                var pots: String = ""
    //
    //                                for (postsnapshot in snapshot.children) {
    //                                    val post = postsnapshot.getValue(Post::class.java)
    //                                    if (post != null && post.postTag == "Exercise") {
    //                                        postArrayList.add(post)
    //                                        ky = postsnapshot.key.toString()
    //                                        pots = post.postTitle.toString()
    //                                        val tmppost = tempData(ky, pots)
    //                                        nodeList.add(tmppost)
    //                                    }
    //                                }
    //
    //
    //                                val adapter = PostAdapter(postArrayList)
    //                                binding.postList.adapter = adapter
    //                                adapter.setOnItemClickListener(object : PostAdapter.OnItemClickListener {
    //                                    override fun onItemClick(position: Int) {
    //                                        val ctpost = nodeList[position]
    //                                        val nodePath = ctpost.id.toString()
    //
    //                                        val bundle = Bundle()
    //                                        bundle.putString("post_id", nodePath)
    //                                        findNavController().navigate(
    //                                            R.id.action_forum_to_forum_details,
    //                                            bundle) }
    //                                })
    //
    //
    //                            } else {
    //                                // Handle case where no Exercise posts are found
    //                                Log.i("Testing123", "No Exercise posts found")
    //
    //                            }
    //                        }
    //
    //                        override fun onCancelled(error: DatabaseError) {
    //
    //                        }
    //
    //                    })
    //                }
    //            }
    //
    //            override fun onCancelled(error: DatabaseError) {
    //
    //            }
    //
    //
    //        })
    //    }




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

                        Log.i("Testing", "postIdssss = $postIds")

                        // Get one post
                        for (postId in postIds) {
                            val query = FirebaseDatabase.getInstance().getReference("Posts").child(postId)

                            query.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        var ky: String = ""
                                        var pots: String = ""

                                        val post = snapshot.getValue(Post::class.java)

                                        if (post != null && post.postTag == "Nutrition") {
                                            Log.i("Testing", "got post")
                                            postArrayList.add(post)
                                            ky = snapshot.key.toString()
                                            pots = post.postTitle.toString()
                                            val tmppost = tempData(ky, pots)
                                            nodeList.add(tmppost)
                                        }

                                        var adapter = PostAdapter(postArrayList)
                                        binding.postList.adapter = adapter
                                        adapter.setOnItemClickListener(object : PostAdapter.OnItemClickListener {
                                            override fun onItemClick(position: Int) {
                                                val ctpost = nodeList[position]
                                                val nodePath = ctpost.id.toString()

                                                val bundle = Bundle()
                                                bundle.putString("post_id",nodePath)

                                                // Set where you wanna go here
                                                //                                                findNavController().navigate(R.id.action_collection_to_forum_details)
                                            }
                                        })

                                        // Here you can update your UI or perform actions with postArrayList
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Handle database error
                                }
                            })
                        }

                        //                        // Create a query to fetch posts with the specified postIds and postType "Nutrition"
                        //                        val query: Query = FirebaseDatabase.getInstance().getReference("Posts")
                        //                            .orderByKey()
                        //                            .startAt(postIds.first()) // Start query at the first postId
                        //                            .endAt(postIds.last())   // End query at the last postId
                        //                            .
                        //
                        //                        query.addValueEventListener(object : ValueEventListener {
                        //                            override fun onDataChange(snapshot: DataSnapshot) {
                        //                                if (snapshot.exists()) {
                        //                                    postArrayList.clear() // Clear the previous data
                        //
                        //                                    for (postsnapshot in snapshot.children) {
                        //                                        val post = postsnapshot.getValue(Post::class.java)
                        //
                        //                                        if (post != null) {
                        //                                            postArrayList.add(post)
                        //                                        }
                        //                                    }
                        //
                        //                                    val adapter = PostAdapter(postArrayList)
                        //                                    binding.postList.adapter = adapter
                        //                                    adapter.setOnItemClickListener(object : PostAdapter.OnItemClickListener {
                        //                                        override fun onItemClick(position: Int) {
                        //                                            val ctpost = nodeList[position]
                        //                                            val nodePath = ctpost.id.toString()
                        //
                        //                                            val bundle = Bundle()
                        //                                            bundle.putString("post_id",nodePath)
                        //                                            findNavController().navigate(R.id.action_forum_to_forum_details, bundle)
                        //                                        }
                        //                                    })
                        //                                } else {
                        //                                    // Handle case where no Nutrition posts are found
                        //                                    Toast.makeText(context, "No Nutrition posts found.", Toast.LENGTH_SHORT).show()
                        //                                }
                        //                            }
                        //
                        //                            override fun onCancelled(error: DatabaseError) {
                        //                                // Handle database error
                        //                            }
                        //                        })
                    } else {
                        // Handle case where the user has no posts in their collection
                        Toast.makeText(context, "You have not collected any posts yet...", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle case where the collection for the user does not exist
                    Toast.makeText(context, "Collection does not exist for this user.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}