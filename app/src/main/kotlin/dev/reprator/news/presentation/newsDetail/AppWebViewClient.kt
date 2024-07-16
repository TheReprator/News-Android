package dev.reprator.news.presentation.newsDetail

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient

class AppWebViewClient (private val isLoaded:(Boolean) -> Unit): WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        // show dialog
        isLoaded(true)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        // hide dialog
        isLoaded(false)
    }
}