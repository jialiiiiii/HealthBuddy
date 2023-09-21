package com.example.healthbuddy.forum

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
import com.example.healthbuddy.databinding.FragmentForumDetailsBinding
import com.example.healthbuddy.post.DtClass
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class ForumDetailsFragment : Fragment() {

    private lateinit var binding: FragmentForumDetailsBinding
    private lateinit var db: DatabaseReference
    var sImage: String? = ""
    var nodeId = ""
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForumDetailsBinding.inflate(inflater, container, false)

        // Initialize shared preferences
        sharedPreferences = requireContext().getSharedPreferences("HealthBuddyPrefs", AppCompatActivity.MODE_PRIVATE)

        // Retrieve the post_id from arguments
        nodeId = arguments?.getString("post_id") ?: ""

        if (nodeId!=""){
            displayData()
        }

        return binding.root
    }

    private val ActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    )
    { result: ActivityResult ->
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

    private fun displayData() {
        db=FirebaseDatabase.getInstance().getReference("Posts")
        db.child(nodeId).get().addOnSuccessListener {
            if(it.exists()){
                val dtClass = DtClass()
                binding.postTitle.text = it.child("postTitle").value.toString()
                binding.postDesc.text = it.child("postDescription").value.toString()
                sImage = it.child("postImage").value.toString()
                val bytes = Base64.decode(sImage, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
                binding.postImg.setImageBitmap(bitmap)
            }
        }
    }
}