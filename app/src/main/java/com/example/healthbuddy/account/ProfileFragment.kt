package com.example.healthbuddy.account

import android.content.Context
import android.content.SharedPreferences
import android.opengl.Visibility
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProfileFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var viewModel: ProfileViewModel
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
        val spinner : Spinner = binding.spinnerGender
        spinner.onItemSelectedListener = this

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gender_selection,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        // From LoginFragment: retrieve data from arguments bundle
        val args = arguments
        if (args != null) {
            val id = args.getString("id")
            val name = args.getString("name")
            val email = args.getString("email")

            binding.titleRegister.visibility = View.VISIBLE
            binding.signOut.visibility  =View.GONE
            binding.save.text = getString(R.string.button_register)

            binding.textEmail.text = email
            binding.editName.setText(name)

        }

        // Set the view's root from the binding object
        return binding.root
    }

    private fun registerUser() {
        // Initialize room database
        val appDatabase = AppDatabase.getDatabase(requireContext())
        userDao = appDatabase.userDao()

        // Initialize realtime database
        db = FirebaseDatabase.getInstance()
        reference = db.getReference("Users")

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("HealthBuddyPrefs", Context.MODE_PRIVATE)

        // Retrieve data from arguments bundle
        val args = arguments
        val id = args?.getString("id") ?: ""
        val name = args?.getString("name")
        val email = args?.getString("email")

        // Create user instance
        val user = User(id = id, name = name, email = email)

        // Store login message
        var loginMsg = ""

        // Insert data into room database using coroutine
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            // Insert data into room database and retrieve the auto-generated ID
            userDao.insertUser(user)
        }

        // Reference to the Firebase Realtime Database node
        val userRef = reference.child(id)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if the data already exists
                if (dataSnapshot.exists()) {
                    // Data exist
                    loginMsg = "Welcome back!"

                    // Navigate to Forum
                    setLoginState(true, loginMsg, id)
                    findNavController().navigate(R.id.action_profile_to_main)
                }
                else{
                    // Data does not exist, add it to realtime database
                    userRef.setValue(user).addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Data added successfully
                            loginMsg = "Welcome to HealthBuddy!"

                            // Navigate to Forum
                            setLoginState(true, loginMsg, id)
                            findNavController().navigate(R.id.action_profile_to_main)
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, databaseError.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setLoginState(loggedIn: Boolean, loginMsg: String? = null, userId: String? = null) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("loggedIn", loggedIn)

        if (loginMsg != null) {
            editor.putString("loginMsg", loginMsg)
        }
        if (userId != null) {
            editor.putString("userId", userId)
        }

        editor.apply()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}