package com.spacece.milestonetracker.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.databinding.ActivityWebViewBinding
import com.spacece.milestonetracker.ui.base.BaseActivity
import com.spacece.milestonetracker.utils.*

class WebViewActivity : BaseActivity(), OnClickListener {
    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindLayout(layoutId = R.layout.activity_web_view)
        setupViewsAndListeners()
        initOnBackPressedDispatcher()
    }

    private fun setupViewsAndListeners() = with(binding) {
        val title = intent.extras?.getString(WEB_VIEW_TITLE)
        val url = intent.extras?.getString(WEB_VIEW_URL)
        setOnClickListeners(listOf(ivBack))
        tvTitle.setupText(title)
        if (url != null) {
            setUpWebView(url)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView(url: String) = with(binding) {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                val pageTitle = view.title
                if (supportActionBar?.title.isNullOrEmpty())
                    supportActionBar?.title = pageTitle
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    progressBar.gone()
                } else {
                    progressBar.visible()
                }
            }
        }
        webView.loadUrl(url)
    }

    private fun initOnBackPressedDispatcher() {
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
}
