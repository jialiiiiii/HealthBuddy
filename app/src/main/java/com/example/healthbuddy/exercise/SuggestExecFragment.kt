package com.example.healthbuddy.exercise

import SuggestExecAdapter
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthbuddy.database.Post
import com.example.healthbuddy.database.Suggestion
import com.example.healthbuddy.databinding.FragmentExecSuggestionBinding
import com.example.healthbuddy.post.TempData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class SuggestExecFragment : Fragment() {
    private lateinit var binding: FragmentExecSuggestionBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var nodeList: ArrayList<TempData>
    private lateinit var execSuggestAdapter: SuggestExecAdapter // Add this adapter
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class
        binding = FragmentExecSuggestionBinding.inflate(inflater, container, false)
        progressBar = binding.progressBar

        // Initialize the adapter with an empty list
        execSuggestAdapter = SuggestExecAdapter(requireContext(), mutableListOf(), this)

        // Set the adapter on your RecyclerView
        binding.exerSuggestionView.layoutManager = LinearLayoutManager(requireContext())
        binding.exerSuggestionView.adapter = execSuggestAdapter

        // Initialize shared preferences
        sharedPreferences = requireContext().getSharedPreferences("HealthBuddyPrefs", AppCompatActivity.MODE_PRIVATE)

        binding.exerSuggestionView.hasFixedSize()
        postArrayList = arrayListOf<Post>()
        nodeList = arrayListOf<TempData>()

        // Retrieve and update data for the RecyclerView
        retrieveAndSortData()

        return binding.root
    }

    fun retrieveAndSortData() {
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

                // Retrieve favorite meal IDs from SharedPreferences
                val favoritePostIds = sharedPreferences.getStringSet("favoriteExecIds", mutableSetOf()) ?: mutableSetOf()

                // Sort the exerciseList based on favorite status
                val sortedList = exerciseList.sortedWith(Comparator { suggestion1, suggestion2 ->
                    val isFavorite1 = favoritePostIds.contains(suggestion1.postId)
                    val isFavorite2 = favoritePostIds.contains(suggestion2.postId)

                    // Sort favorites first, non-favorites last
                    if (isFavorite1 && !isFavorite2) {
                        -1
                    } else if (!isFavorite1 && isFavorite2) {
                        1
                    } else {
                        // If both are favorites or both are non-favorites, maintain their original order
                        0
                    }
                })

                // Update the adapter with the retrieved data
                execSuggestAdapter.updateData(sortedList)

                // Hide the progress bar when data retrieval is complete
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error

                // Hide the progress bar in case of an error
                progressBar.visibility = View.GONE
            }
        })
    }

    fun scrollTop() {
        binding.exerSuggestionView.scrollToPosition(0)
    }
}