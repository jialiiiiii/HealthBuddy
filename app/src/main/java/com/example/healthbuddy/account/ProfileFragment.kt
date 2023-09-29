package com.example.healthbuddy.account

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.healthbuddy.R
import com.example.healthbuddy.database.AppDatabase
import com.example.healthbuddy.database.User
import com.example.healthbuddy.database.UserDao
import com.example.healthbuddy.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class ProfileFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var userDao: UserDao
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var db: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Set up gender spinner
        val spinner: Spinner = binding.spinnerGender
        spinner.onItemSelectedListener = this

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gender_selection,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        // Initialize room database
        val appDatabase = AppDatabase.getDatabase(requireContext())
        userDao = appDatabase.userDao()

        // Initialize realtime database
        db = FirebaseDatabase.getInstance()
        reference = db.getReference("Users")

        // Initialize SharedPreferences
        sharedPreferences =
            requireContext().getSharedPreferences("HealthBuddyPrefs", Context.MODE_PRIVATE)

        // From LoginFragment: retrieve data from arguments bundle
        val args = arguments
        if (args != null) {
            val id = args.getString("id", "")
            val name = args.getString("name")
            val email = args.getString("email", "")

            binding.titleRegister.visibility = View.VISIBLE
            binding.save.visibility = View.GONE
            binding.signOut.visibility = View.GONE
            binding.register.visibility = View.VISIBLE

            binding.textEmail.text = email
            binding.editName.setText(name)

            binding.register.setOnClickListener {
                if (validateInput()) {
                    registerUser(id, email)
                }
            }

        }

        // Get user id
        val userId = sharedPreferences.getString("userId", "") ?: ""

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            // Get user data
            val user = userDao.getUserById(userId)

            if (user != null) {
                // Switch to the main dispatcher for UI updates
                withContext(Dispatchers.Main) {
                    binding.editName.setText(user.name)
                    binding.textEmail.text = user.email
                    binding.editAge.setText(user.age?.toString() ?: "")
                    binding.editWeight.setText(user.weight?.toString() ?: "")
                    binding.editHeight.setText(user.height?.toString() ?: "")

                    // Set value for gender spinner
                    val adapter = binding.spinnerGender.adapter as ArrayAdapter<String>
                    val position = adapter.getPosition(user.gender)
                    binding.spinnerGender.setSelection(position)
                }
            }

        }

        binding.save.setOnClickListener {
            if (validateInput()) {
                updateUser(userId)
            }
        }

        binding.signOut.setOnClickListener {
            // Ask for confirmation
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.signing_out)
                .setMessage(R.string.sign_out_msg)
                .setPositiveButton(R.string.confirm_sign_out) { dialog, which ->
                    signOut()
                }
                .setNegativeButton(R.string.cancel_sign_out) { dialog, which ->
                    // User canceled sign out, do nothing or handle as needed
                }
                .show()
        }

        // Set the view's root from the binding object
        return binding.root
    }

    private fun signOut() {
        // Clear cache
        val cacheDir = requireContext().cacheDir
        deleteRecursive(cacheDir)

        // Clear SharedPreferences
        sharedPreferences.edit().clear().apply()

        // Sign the user out
        FirebaseAuth.getInstance().signOut()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        googleSignInClient.signOut().addOnCompleteListener(requireActivity()) { signOutTask ->
                if (signOutTask.isSuccessful) {
                    findNavController().navigate(R.id.action_profile_to_landing)
                }
            }
    }

    private fun validateInput(): Boolean {
        var result = true

        val name = binding.editName.text.toString().trim()
        val age = binding.editAge.text.toString().trim()
        val weight = binding.editWeight.text.toString().trim()
        val height = binding.editHeight.text.toString().trim()

        var nameErr = ""
        var ageErr = ""
        var weightErr = ""
        var heightErr = ""

        if (name.isNullOrEmpty()) {
            nameErr = getString(R.string.name_null)
            result = false
        }

        if (age.isNullOrEmpty()) {
            ageErr = getString(R.string.age_null)
            result = false
        } else {
            val ageValue = age.toIntOrNull()

            if (ageValue == null || ageValue < 10 || ageValue > 100) {
                ageErr = getString(R.string.age_invalid)
                result = false
            }
        }

        if (weight.isNullOrEmpty()) {
            weightErr = getString(R.string.weight_null)
            result = false
        } else {
            val weightValue = weight.toDoubleOrNull()

            if (weightValue == null || weightValue < 20 || weightValue > 500) {
                weightErr = getString(R.string.weight_invalid)
                result = false
            }
        }

        if (height.isNullOrEmpty()) {
            heightErr = getString(R.string.height_null)
            result = false
        } else {
            val heightValue = weight.toDoubleOrNull()

            if (heightValue == null || heightValue < 50 || heightValue > 250) {
                heightErr = getString(R.string.height_invalid)
                result = false
            }
        }

        var hasFocus = false
        if (!nameErr.isNullOrEmpty()) {
            binding.editName.error = nameErr
            binding.editName.requestFocus()
            hasFocus = true
        }
        if (!ageErr.isNullOrEmpty()) {
            binding.editAge.error = ageErr
            if (!hasFocus) {
                binding.editAge.requestFocus()
                hasFocus = true
            }
        }
        if (!weightErr.isNullOrEmpty()) {
            binding.editWeight.error = weightErr
            if (!hasFocus) {
                binding.editWeight.requestFocus()
                hasFocus = true
            }

        }
        if (!heightErr.isNullOrEmpty()) {
            binding.editHeight.error = heightErr
            if (!hasFocus) {
                binding.editHeight.requestFocus()
            }
        }

        return result
    }

    private fun updateUser(id: String) {
        val name = binding.editName.text.toString().trim()
        val gender = binding.spinnerGender.selectedItem.toString()
        val age = binding.editAge.text.toString().trim().toInt()
        val weight = binding.editWeight.text.toString().trim().toDouble()
        val height = binding.editHeight.text.toString().trim().toDouble()

        // Update room db
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            // Check if the user with the same ID exists
            var user = userDao.getUserById(id)

            if (user != null) {
                // User exists, update the user data
                user.name = name
                user.gender = gender
                user.age = age
                user.weight = weight
                user.height = height

                // Update the user in the room database
                userDao.updateUser(user)
            }
        }

        // Update realtime db
        val userUpdates = HashMap<String, Any>()
        userUpdates["name"] = name
        userUpdates["gender"] = gender
        userUpdates["age"] = age
        userUpdates["weight"] = weight
        userUpdates["height"] = height

        // Reference to the Firebase Realtime Database node
        val userRef = reference.child(id)

        userRef.updateChildren(userUpdates).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, R.string.profile_updated, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun registerUser(id: String, email: String) {

        val name = binding.editName.text.toString().trim()
        val gender = binding.spinnerGender.selectedItem.toString()
        val age = binding.editAge.text.toString().trim().toInt()
        val weight = binding.editWeight.text.toString().trim().toDouble()
        val height = binding.editHeight.text.toString().trim().toDouble()

        // Create user instance
        val user = User(id, name, email, gender, age, weight, height)

        // Insert data into room database using coroutine
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            // Check if the user with the same ID exists
            val existingUser = userDao.getUserById(id)

            if (existingUser == null) {
                // User doesn't exist, insert it into the room database
                userDao.insertUser(user)
            }
        }

        // Reference to the Firebase Realtime Database node
        val userRef = reference.child(id)

        userRef.setValue(user).addOnCompleteListener {
            if (it.isSuccessful) {
                // Data added successfully
                val editor = sharedPreferences.edit()
                editor.putBoolean("loggedIn", true)
                editor.putString("loginMsg", resources.getString(R.string.welcome_msg))
                editor.putString("userId", id)
                editor.apply()

                findNavController().navigate(R.id.action_profile_to_main)
            }
        }
    }

    private fun deleteRecursive(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory) {
            for (child in fileOrDirectory.listFiles() ?: emptyArray()) {
                deleteRecursive(child)
            }
        }
        fileOrDirectory.delete()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}