package com.example.healthbuddy.exercise

import SuggestExecAdapter
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthbuddy.database.Post
import com.example.healthbuddy.database.Suggestion
import com.example.healthbuddy.databinding.FragmentExecSuggestionBinding
import com.example.healthbuddy.post.RecyclerViewItemDecoration
import com.example.healthbuddy.post.tempData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class SuggestExecFragment : Fragment() {
    private lateinit var binding: FragmentExecSuggestionBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var nodeList: ArrayList<tempData>
    private lateinit var execSuggestAdapter: SuggestExecAdapter // Add this adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class
        binding = FragmentExecSuggestionBinding.inflate(inflater, container, false)

        // Initialize the adapter with an empty list
        execSuggestAdapter = SuggestExecAdapter(requireContext(), mutableListOf())

        // Set the adapter on your RecyclerView
        binding.exerSuggestionView.layoutManager = LinearLayoutManager(requireContext())
        binding.exerSuggestionView.adapter = execSuggestAdapter

        // Initialize shared preferences
        sharedPreferences = requireContext().getSharedPreferences("HealthBuddyPrefs", AppCompatActivity.MODE_PRIVATE)

        // Add item decoration for spacing
        val itemDecoration = RecyclerViewItemDecoration(15, 2)
        binding.exerSuggestionView.addItemDecoration(itemDecoration)

        binding.exerSuggestionView.hasFixedSize()
        postArrayList = arrayListOf<Post>()
        nodeList = arrayListOf<tempData>()

        // Retrieve and update data for the RecyclerView
        retrieveData()

        return binding.root
    }

    private fun retrieveData() {
        val db = FirebaseDatabase.getInstance().getReference("Exercises")

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val exerciseList = mutableListOf<Suggestion>()

                for (postSnapshot in dataSnapshot.children) {
                    val postId = postSnapshot.key.toString()
                    val postTitle = postSnapshot.child("postTitle").value.toString()
                    val postDescription = postSnapshot.child("postDescription").value.toString()
                    val postImageBase64 = postSnapshot.child("postImage").value.toString()

                    // Convert the base64 image to a Bitmap
                    val bytes = Base64.decode(postImageBase64, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                    // Create a Suggestion object
                    val suggestion = Suggestion(postId, postTitle, postDescription, bitmap)
                    exerciseList.add(suggestion)
                }

                // Update the adapter with the retrieved data
                execSuggestAdapter.updateData(exerciseList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }
}