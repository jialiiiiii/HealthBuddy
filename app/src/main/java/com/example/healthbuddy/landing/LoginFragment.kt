package com.example.healthbuddy.landing

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.TextAppearanceSpan
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.healthbuddy.R
import com.example.healthbuddy.databinding.LandingLoginFragmentBinding

class LoginFragment : Fragment() {

    private lateinit var binding: LandingLoginFragmentBinding
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

        // Set the view's root from the binding object
        return binding.root
    }

}