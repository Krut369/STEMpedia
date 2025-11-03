package com.example.contentsharingapp

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.contentsharingapp.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val type = intent.getStringExtra("type") ?: "content"
        val targetUrl = intent.getStringExtra("targetUrl")
        val youtubeId = intent.getStringExtra("youtubeId")

        setupWebView()
        setupBackPressHandler()

        when (type) {
            "youtube" -> {
                val finalYoutubeId = youtubeId ?: extractYoutubeId(targetUrl)
                if (!finalYoutubeId.isNullOrBlank()) {
                    loadYouTubeVideo(finalYoutubeId)
                } else {
                    binding.webView.loadData(
                        "<html><body><h3>Invalid YouTube ID</h3></body></html>",
                        "text/html",
                        "utf-8"
                    )
                }
            }
            else -> {
                if (!targetUrl.isNullOrBlank()) {
                    binding.webView.loadUrl(targetUrl)
                } else {
                    binding.webView.loadData(
                        "<html><body><h3>No content available</h3></body></html>",
                        "text/html",
                        "utf-8"
                    )
                }
            }
        }
    }

    private fun setupWebView() {
        binding.webView.webViewClient = WebViewClient()
        binding.webView.webChromeClient = WebChromeClient()
        val settings: WebSettings = binding.webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.mediaPlaybackRequiresUserGesture = false
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
                } else {
                    finish()
                }
            }
        })
    }

    private fun loadYouTubeVideo(youtubeId: String) {
        val html = """
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin:0;padding:0;">
                <iframe 
                    width="100%" 
                    height="100%" 
                    src="https://www.youtube.com/embed/$youtubeId?autoplay=0&modestbranding=1" 
                    frameborder="0" 
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" 
                    allowfullscreen>
                </iframe>
            </body>
            </html>
        """.trimIndent()

        binding.webView.loadDataWithBaseURL(
            "https://www.youtube.com",
            html,
            "text/html",
            "utf-8",
            null
        )
    }

    private fun extractYoutubeId(url: String?): String? {
        if (url == null) return null
        val regex = "(?<=v=)[^&]+|(?<=be/)[^?&]+".toRegex()
        val match = regex.find(url)
        return match?.value ?: run {
            if (url.length >= 11) url.takeLast(11) else null
        }
    }

}