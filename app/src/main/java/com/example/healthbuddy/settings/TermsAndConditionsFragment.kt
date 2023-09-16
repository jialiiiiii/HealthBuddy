package com.example.healthbuddy.com.example.healthbuddy.settings

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import com.example.healthbuddy.R
import com.example.healthbuddy.databinding.TermsAndConditionsFragmentBinding
import java.io.IOException
import java.io.InputStream

class TermsAndConditionsFragment : Fragment() {

    private lateinit var binding: TermsAndConditionsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.terms_and_conditions_fragment,
            container,
            false
        )

        binding.imageView.setOnClickListener {
            activity?.onBackPressed()
        }

        // Find the WebView
        val webView = binding.webView
        val progressBar = binding.progressBar

        // Determine the selected language
        val selectedLanguage = "en"

        // Define the file name based on the selected language
        val fileName = "terms_and_conditions_$selectedLanguage.html"

        try {
            // Open and read the HTML file from the assets folder
            val assetManager = requireContext().assets
            val inputStream: InputStream = assetManager.open(fileName)

            // Enable JavaScript for a better WebView experience
            webView.settings.javaScriptEnabled = true

            // Load the HTML content into the WebView
            webView.loadDataWithBaseURL(null, inputStream.reader().readText(), "text/html", "UTF-8", null)

            // Ensure links open in the WebView, not in an external browser
            webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    // Show the progress bar when a page starts loading
                    progressBar.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    // Hide the progress bar when the page has finished loading
                    progressBar.visibility = View.GONE
                }
            }

        } catch (e: IOException) {
            // Handle any exceptions that may occur while reading the file
            e.printStackTrace()
        }

        return binding.root
    }

}