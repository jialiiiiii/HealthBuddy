package com.example.healthbuddy

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class TermsAndConditionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.terms_and_conditions_fragment, container, false)

        // Find the TextView
        val content = view.findViewById<TextView>(R.id.text_content)

        // Get the HTML-formatted string from resources
        val htmlText = getString(R.string.text_terms_and_conditions)

        // Check Android version and set the text with HTML formatting
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            content.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
        } else {
            content.text = Html.fromHtml(htmlText)
        }

        return view
    }

}