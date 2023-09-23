package com.example.healthbuddy.forum

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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
import com.example.healthbuddy.databinding.FragmentForumBinding
import com.example.healthbuddy.post.PostAdapter
import com.example.healthbuddy.post.tempData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class CollectionFragment : Fragment() {

    private lateinit var binding: FragmentCollectionBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var db: DatabaseReference
    var sImage: String? = ""
    var nodeId = ""

    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var nodeList: ArrayList<tempData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

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
        binding.postList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.postList.hasFixedSize()
        postArrayList = arrayListOf<Post>()
        nodeList = arrayListOf<tempData>()
        getItemData()



        return binding.root
    }

    private fun getItemData() {
        db = FirebaseDatabase.getInstance().getReference("Posts")
        val query: Query = db.orderByChild("bookMark").equalTo("Yes")


        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var ky: String = ""
                    var pots: String = ""

                    for (postsnapshot in snapshot.children) {
                        val post = postsnapshot.getValue(Post::class.java)

                        if(post != null && post.postTag == "Nutrition") {
                            postArrayList.add(post!!)
                            ky = postsnapshot.key.toString()
                            pots = post.postTitle.toString()
                            val tmppost = tempData(ky, pots)
                            nodeList.add(tmppost)
                        }else{
                            Toast.makeText(context,"Opps, seems like you have not collected any post yet...",Toast.LENGTH_SHORT).show()
                        }
                    }

                    var adapter = PostAdapter(postArrayList)
                    binding.postList.adapter = adapter
                    adapter.setOnItemClickListener(object : PostAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val ctpost = nodeList[position]
                            val nodePath = ctpost.id.toString()
                            val fragment = CollectionFragment()
                            //val fragment = PostFragment()
                            val bundle=Bundle()
                            bundle.putString("post_id",nodePath.toString())
//                            findNavController().navigate(R.id.action_forumFragment2_to_forumDetailsFragment,bundle)
                            //findNavController().navigate(R.id.action_forumFragment2_to_editPostFragment2,bundle)
                            //findNavController().navigate(R.id.action_forumFragment2_to_addPostFragment2,bundle)
                            //findNavController().navigate(R.id.action_forumFragment2_to_collectionFragment,bundle)
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}