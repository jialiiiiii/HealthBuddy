package com.example.healthbuddy.post

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.Visibility
import android.os.Bundle
import android.util.Base64
import android.util.Log
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
import androidx.navigation.fragment.findNavController
import com.example.healthbuddy.R
import com.example.healthbuddy.database.Post
import com.example.healthbuddy.databinding.FragmentAddPostBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class AddPostFragment : Fragment() {

    private lateinit var binding: FragmentAddPostBinding
    private lateinit var db: DatabaseReference
    var sImage: String? = ""
    private lateinit var sharedPreferences: SharedPreferences

    private var isImageSelected = false
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
                isImageSelected = true

                // Change UI
                binding.selectImgButton.visibility = View.GONE
                binding.postImg.visibility = View.VISIBLE
                binding.postImg.setOnClickListener {
                    binding.selectImgButton.performClick()
                }

            } catch (ex: Exception) {
                Toast.makeText(context, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }
        } else {
            // No image selected, set sImage to an empty string
            sImage = ""
            isImageSelected = false


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
            requireContext(), R.array.spinner_options, R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                //binding.postTag.text = selectedItem
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

        binding.cancelButton.setOnClickListener(){
            findNavController().navigateUp()
        }

        return root
    }

    private fun addPost() {
        val userId = sharedPreferences.getString("userId", "")
        val tag = binding.postTagSpinner.selectedItem.toString()
        val title = binding.titleText.text.toString()
        val description = binding.descriptionText.text.toString()

        // Check if any of the fields are empty
        if (title.isEmpty() || description.isEmpty()|| !isImageSelected) {
            if (title.isEmpty()) {
                binding.titleText.error = getString(R.string.title_empty)
            }
            if (description.isEmpty()) {
                binding.descriptionText.error = getString(R.string.description_empty)
            }
            if(!isImageSelected){
                // Show a Snackbar message here to indicate that no image is selected
                Snackbar.make(requireView(), getString(R.string.image_empty), Snackbar.LENGTH_SHORT).show()
            }
            return // Exit the function without adding the post
        }

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
            binding.postImg.visibility = View.GONE
            binding.selectImgButton.visibility = View.VISIBLE
            Toast.makeText(context, getString(R.string.post_created), Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, getString(R.string.post_failed), Toast.LENGTH_SHORT).show()
        }
    }

}
