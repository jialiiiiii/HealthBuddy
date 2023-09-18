package com.example.healthbuddy.landing

import android.app.Activity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.healthbuddy.R
import com.example.healthbuddy.databinding.LandingLoginFragmentBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {

    private lateinit var binding: LandingLoginFragmentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
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

        // Google sign-in
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

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
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                // The user has successfully signed in with Google
                val user = auth.currentUser
                val id = user?.uid ?: ""
                val name = user?.displayName
                val email = user?.email

                // Create a Firestore reference to the user's document (replace 'users' with your collection name)
                val usersCollection = FirebaseFirestore.getInstance().collection("users")
                val userDocument = usersCollection.document(id)

                // Check if the document already exists
                userDocument.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (!documentSnapshot.exists()) {
                            // Document does not exist; create and write data
                            val userData = HashMap<String, Any>()
                            if (name != null) {
                                userData["name"] = name
                            }
                            if (email != null) {
                                userData["email"] = email
                            }

                            userDocument.set(userData)
                                .addOnSuccessListener {
                                    // Data has been successfully written to Firestore
                                }
                                .addOnFailureListener { e ->
                                    // Handle the error if data couldn't be written to Firestore
                                    Toast.makeText(context, "Error writing user data to Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                                }

//                            // Create database instance
//                            val db = Room.databaseBuilder(
//                                requireContext(),
//                                HealthDatabase::class.java, "health-database"
//                            ).build()
//
//                            // Insert data into Room Database
//                            val user = User(id, name, email)
//                            GlobalScope.launch(Dispatchers.IO) {
//                                db.userDao().insert(user)
//                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        // Handle the error if checking document existence fails
                        Toast.makeText(context, "Error checking user data in Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

                // Navigate to Profile
            }
            else{
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

}