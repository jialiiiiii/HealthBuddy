package com.example.healthbuddy.landing

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.healthbuddy.R
import com.example.healthbuddy.database.UserDao
import com.example.healthbuddy.databinding.LandingLoginFragmentBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginFragment : Fragment() {

    private lateinit var binding: LandingLoginFragmentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var userDao: UserDao
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var db: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.landing_login_fragment,
            container,
            false
        )

//        // Initialize room database
//        val appDatabase = AppDatabase.getDatabase(requireContext())
//        userDao = appDatabase.userDao()
//
//        // Initialize realtime database
//        db = FirebaseDatabase.getInstance()
//        reference = db.getReference("Users")
//
//        // Initialize SharedPreferences
//        sharedPreferences = requireContext().getSharedPreferences("HealthBuddyPrefs", Context.MODE_PRIVATE)

        // Find the TextView that contains disclaimer
        val disclaimerTextView = binding.disclaimer

        // Retrieve text from strings.xml
        val termsAndConditionsText = getString(R.string.terms_and_conditions)
        val privacyPolicyText = getString(R.string.privacy_policy)
        val disclaimerTemplate = getString(R.string.disclaimer)

        // Replace placeholders with underlined and clickable text
        val spannableDisclaimer = SpannableString(disclaimerTemplate)

        // Find the positions of "Terms and Conditions" and "Privacy Policy" within the disclaimer text
        val termsAndConditionsStart = spannableDisclaimer.indexOf(termsAndConditionsText)
        val privacyPolicyStart = spannableDisclaimer.indexOf(privacyPolicyText)

        // Apply underline and click listener to "Terms and Conditions"
        spannableDisclaimer.setSpan(
            UnderlineSpan(),
            termsAndConditionsStart,
            termsAndConditionsStart + termsAndConditionsText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableDisclaimer.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    // Navigate to the Terms and Conditions fragment
                    findNavController().navigate(R.id.action_login_to_terms)
                }
            },
            termsAndConditionsStart,
            termsAndConditionsStart + termsAndConditionsText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Apply underline and click listener to "Privacy Policy"
        spannableDisclaimer.setSpan(
            UnderlineSpan(),
            privacyPolicyStart,
            privacyPolicyStart + privacyPolicyText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableDisclaimer.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    // Navigate to the Privacy Policy fragment
                    findNavController().navigate(R.id.action_login_to_privacy)
                }
            },
            privacyPolicyStart,
            privacyPolicyStart + privacyPolicyText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set the formatted text in the TextView and enable clickable links
        disclaimerTextView.text = spannableDisclaimer
        disclaimerTextView.movementMethod = LinkMovementMethod.getInstance()

        // Initialize google sign-In client
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        // Set listener for login button
        binding.button.setOnClickListener{
            signInGoogle()
        }

        // Set the view's root from the binding object
        return binding.root
    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
            if(result.resultCode == Activity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>){
        if(task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if(account != null){
                updateUI(account)
            }
        }
        else{
            Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount){
        // Find the ProgressBar
        val loadingContainer = binding.loadingContainer
        val button = binding.button

        // Show the ProgressBar
        loadingContainer.visibility = View.VISIBLE
        button.isEnabled = false

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                val authUser = auth.currentUser
                val id = authUser?.uid ?: ""
                val name = authUser?.displayName
                val email = authUser?.email

                val bundle = Bundle()
                bundle.putString("id", id)
                bundle.putString("name", name)
                bundle.putString("email", email)

                findNavController().navigate(R.id.action_login_to_profile, bundle)
            }
            else{
                // Hide the loading indicator in case of an error
                loadingContainer.visibility = View.GONE
                button.isEnabled = true

                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

}