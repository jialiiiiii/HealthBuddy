package com.example.healthbuddy.admin

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.healthbuddy.database.Post
import com.example.healthbuddy.database.Suggestion
import com.example.healthbuddy.database.Suggestionss
import com.example.healthbuddy.databinding.FragmentAddDefaultExecBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class AddDefaultExec : Fragment() {

    private lateinit var binding: FragmentAddDefaultExecBinding
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
        binding = FragmentAddDefaultExecBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize shared preferences
        sharedPreferences = requireContext().getSharedPreferences("HealthBuddyPrefs", AppCompatActivity.MODE_PRIVATE)

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
        val title = binding.titleText.text.toString()
        val description = binding.descriptionText.text.toString()

//        db = FirebaseDatabase.getInstance().getReference("Exercises")
        db = FirebaseDatabase.getInstance().getReference("Nutritions")

        val post = Suggestionss(title, description, sImage)
        val databaseReference = FirebaseDatabase.getInstance().reference
        val id = databaseReference.push().key

        db.child(id.toString()).setValue(post).addOnSuccessListener {
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