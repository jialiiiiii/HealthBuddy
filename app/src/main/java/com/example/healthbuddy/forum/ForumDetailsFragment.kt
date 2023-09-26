package com.example.healthbuddy.forum

import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.healthbuddy.R
import com.example.healthbuddy.database.Collection
import com.example.healthbuddy.databinding.FragmentForumDetailsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ForumDetailsFragment : Fragment() {

    private lateinit var binding: FragmentForumDetailsBinding
    private lateinit var db: DatabaseReference
    var sImage: String? = ""
    var nodeId = ""
    var userId = ""
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentForumDetailsBinding.inflate(inflater, container, false)

        // Initialize shared preferences
        sharedPreferences = requireContext().getSharedPreferences(
            "HealthBuddyPrefs",
            AppCompatActivity.MODE_PRIVATE
        )

        // Retrieve the post_id from arguments
        nodeId = arguments?.getString("post_id") ?: ""

        // Get user id
        userId = sharedPreferences.getString("userId", "") ?: ""

        if (nodeId != "") {
            displayData()
        }

        // Handle bookmark action
        val bookMark = binding.bookMark
        bookMark.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                setBookmark(true)
            } else {
                setBookmark(false)
            }
        }

        bookMark.setOnClickListener {
            val checked = bookMark.isChecked
            if (checked) {
                Toast.makeText(context, R.string.add_collection, Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                Toast.makeText(context, R.string.remove_collection, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.backButton.setOnClickListener() {
            findNavController().navigateUp()
        }


        return binding.root
    }

    private fun setBookmark(bookmark: Boolean): Boolean {
        var result = false
        db = FirebaseDatabase.getInstance().getReference("Collections")

        // Retrieve data
        db.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var existing = dataSnapshot.getValue(Collection::class.java)

                if (existing?.postId != null) {
                    val postIds = existing.postId

                    if (bookmark) {
                        // If bookmark is true
                        // If postId is not in the ArrayList, add postId
                        if (!postIds.contains(nodeId)) {
                            postIds.add(nodeId)
                            result = true
                        }
                    } else {
                        // If bookmark is false
                        // If postId is in the ArrayList, remove postId
                        if (postIds.contains(nodeId)) {
                            postIds.remove(nodeId)
                            result = true
                        }
                    }

                    existing.postId = postIds
                    db.child(userId).setValue(existing)

                } else {
                    // Create a new Collection object if it doesn't exist
                    val new = Collection(userId = userId, postId = ArrayList())

                    if (bookmark) {
                        // If bookmark is true, add postId to the new ArrayList
                        new.postId?.add(nodeId)
                        result = true
                    }

                    db.child(userId).setValue(new)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors if needed
            }
        })

        return result
    }

    private fun displayData() {
        // Check bookmark status
        db = FirebaseDatabase.getInstance().getReference("Collections")
        db.child(userId).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val collection = dataSnapshot.getValue(Collection::class.java)
                val postIds = collection?.postId
                binding.bookMark.isChecked = postIds != null && postIds.contains(nodeId)
            }
        }

        db = FirebaseDatabase.getInstance().getReference("Posts")
        db.child(nodeId).get().addOnSuccessListener { it ->
            if (it.exists()) {
                binding.postTitle.text = it.child("postTitle").value.toString()
                binding.postDesc.text = it.child("postDescription").value.toString()
                sImage = it.child("postImage").value.toString()
                val bytes = Base64.decode(sImage, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                binding.postImg.setImageBitmap(bitmap)

                // Display author name
                val postUserId = it.child("postUser").value.toString()

                db = FirebaseDatabase.getInstance().getReference("Users")
                db.child(postUserId).get().addOnSuccessListener { userSnapshot ->
                    if (userSnapshot.exists()) {
                        val authorName = getString(R.string.author) + ": " + userSnapshot.child("name").value.toString()
                        binding.author.text = authorName
                    }
                }
            }
        }

    }
}