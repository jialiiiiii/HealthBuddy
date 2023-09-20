package com.example.healthbuddy.post

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.healthbuddy.R
import com.example.healthbuddy.database.Post
import com.example.healthbuddy.databinding.FragmentAddPostBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class AddPostFragment : Fragment() {

    private lateinit var binding: FragmentAddPostBinding
    private lateinit var db: DatabaseReference
    var sImage: String? = ""
    private lateinit var sharedPreferences: SharedPreferences

    private val ActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val uri = result.data!!.data

            try {
                val inputStream = context?.contentResolver?.openInputStream(uri!!)
                val myBitmap = BitmapFactory.decodeStream(inputStream)
                val stream = ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val bytes = stream.toByteArray()
                sImage = Base64.encodeToString(bytes, Base64.DEFAULT)
                binding.postImg.setImageBitmap(myBitmap)
                inputStream!!.close()
                Toast.makeText(context, "Image Selected", Toast.LENGTH_SHORT).show()
            } catch (ex: Exception) {
                Toast.makeText(context, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize shared preferences
        sharedPreferences = requireContext().getSharedPreferences("HealthBuddyPrefs", AppCompatActivity.MODE_PRIVATE)

        // Initialize the Spinner
        val spinner = binding.postTagSpinner

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(), R.array.spinner_options, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                binding.postTag.text = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { // Do nothing
            }
        }

        binding.selectImgButton.setOnClickListener() {
            val myfileintent = Intent(Intent.ACTION_GET_CONTENT)
            myfileintent.type = "image/*"
            ActivityResultLauncher.launch(myfileintent)
        }

        binding.createPostButton.setOnClickListener() {
            addPost()
        }

        return root
    }

    private fun addPost() {
        val userId = sharedPreferences.getString("userId", "")

        val tag = binding.postTag.text.toString()
        val title = binding.titleText.text.toString()
        val description = binding.descriptionText.text.toString()

        db = FirebaseDatabase.getInstance().getReference("Posts")

        val post = Post(tag, title, description, sImage, userId)
        val databaseReference = FirebaseDatabase.getInstance().reference
        val id = databaseReference.push().key

        db.child(id.toString()).setValue(post).addOnSuccessListener {
            binding.postTagSpinner.setSelection(-1)
            binding.titleText.text.clear()
            binding.descriptionText.text.clear()
            sImage = ""
            binding.postImg.setImageBitmap(null)
            Toast.makeText(context, "Post Created!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Fail to create post!", Toast.LENGTH_SHORT).show()
        }
    }
}
