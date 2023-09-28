package com.example.healthbuddy.post

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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.healthbuddy.R
import com.example.healthbuddy.com.example.healthbuddy.forum.ForumFragment
import com.example.healthbuddy.database.Post
import com.example.healthbuddy.databinding.FragmentEditPostBinding
import com.example.healthbuddy.databinding.FragmentPostBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream


class EditPostFragment : Fragment() {

    private lateinit var binding: FragmentEditPostBinding
    private lateinit var db: DatabaseReference
    var sImage: String? = ""
    var nodeId = ""
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class
        binding = FragmentEditPostBinding.inflate(inflater, container, false)

        val top = binding.layoutTop
        top.iconSettings.setOnClickListener {
            findNavController().navigate(R.id.action_edit_post_to_settings)
        }

        binding.imageView.setOnClickListener {
            activity?.onBackPressed()
        }

        // Initialize shared preferences
        sharedPreferences = requireContext().getSharedPreferences("HealthBuddyPrefs", AppCompatActivity.MODE_PRIVATE)

        // Initialize the Spinner
        val spinner = binding.postTagSpinner

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
              //  binding.postTag.text = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        binding.selectImgButton.setOnClickListener() {
            val myfileintent = Intent(Intent.ACTION_GET_CONTENT)
            myfileintent.type = "image/*"
            ActivityResultLauncher.launch(myfileintent)
        }

        // Retrieve the post_id from arguments
        nodeId = arguments?.getString("post_id") ?: ""

        if (nodeId!=""){
            displayData()
        }

        binding.savePostButton.setOnClickListener() {
            updateData()
        }

        binding.delPostButton.setOnClickListener(){
            deleteData()
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

                // Change UI
                binding.selectImgButton.visibility = View.GONE
                binding.postImg.visibility = View.VISIBLE
                binding.postImg.setOnClickListener {
                    binding.selectImgButton.performClick()
                }

            } catch (ex: Exception) {
                Toast.makeText(context, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteData() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle(R.string.del_confirmation)
        alertDialogBuilder.setMessage(R.string.del_confirmation_msg_post)
        alertDialogBuilder.setPositiveButton(R.string.del_confirm) { _, _ ->
            // User confirmed deletion
            db = FirebaseDatabase.getInstance().getReference("Posts")
            db.child(nodeId).removeValue().addOnSuccessListener {
                binding.postTagSpinner.setSelection(-1)
                binding.titleText.text.clear()
                binding.descriptionText.text.clear()
                sImage = ""
                binding.postImg.setImageBitmap(null)
                binding.savePostButton.visibility = View.INVISIBLE
                binding.delPostButton.visibility = View.INVISIBLE

                Toast.makeText(context, getString(R.string.post_deleted), Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { exception ->
                Toast.makeText(context, getString(R.string.post_delete_failed), Toast.LENGTH_SHORT).show()
            }
        }
        alertDialogBuilder.setNegativeButton(R.string.del_cancel) { dialog, _ ->
            // User canceled deletion
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    private fun updateData() {
        val userId = sharedPreferences.getString("userId", "")
        val tag = binding.postTagSpinner.selectedItem.toString()
       // val tag = binding.postTag.text.toString()
        val title = binding.titleText.text.toString()
        val description = binding.descriptionText.text.toString()

        // Check if any of the fields are empty
        if (title.isEmpty() || description.isEmpty()) {
            if (title.isEmpty()) {
                binding.titleText.error = getString(R.string.title_empty)
            }
            if (description.isEmpty()) {
                binding.descriptionText.error = getString(R.string.description_empty)
            }
            return // Exit the function without adding the post
        }

        db = FirebaseDatabase.getInstance().getReference("Posts")
        val post = Post(tag, title, description, sImage, userId)
        db.child(nodeId).setValue(post).addOnSuccessListener {
            binding.postTagSpinner.setSelection(-1)
            binding.titleText.text.clear()
            binding.descriptionText.text.clear()
            sImage = ""
            binding.postImg.setImageBitmap(null)
            binding.savePostButton.visibility = View.INVISIBLE
            binding.delPostButton.visibility = View.INVISIBLE

            Toast.makeText(context, getString(R.string.post_updated), Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(context, getString(R.string.post_update_failed), Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayData() {
        db = FirebaseDatabase.getInstance().getReference("Posts")
        db.child(nodeId).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val postTag = dataSnapshot.child("postTag").value.toString()
                binding.postTagSpinner.setSelection(getIndexOfValue(binding.postTagSpinner, postTag))
                binding.titleText.setText(dataSnapshot.child("postTitle").value.toString())
                binding.descriptionText.setText(dataSnapshot.child("postDescription").value.toString())
                sImage = dataSnapshot.child("postImage").value.toString()
                val bytes = Base64.decode(sImage, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                binding.postImg.setImageBitmap(bitmap)

                // Set UI
                binding.selectImgButton.visibility = View.GONE
                binding.postImg.visibility = View.VISIBLE
                binding.postImg.setOnClickListener {
                    binding.selectImgButton.performClick()
                }

                binding.delPostButton.visibility = View.VISIBLE
                binding.savePostButton.visibility = View.VISIBLE
            }
        }
    }

    private fun getIndexOfValue(spinner: Spinner, value: String): Int {
        val adapter = spinner.adapter
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).toString() == value) {
                return i
            }
        }
        return 0 // Default to the first item if not found
    }

}