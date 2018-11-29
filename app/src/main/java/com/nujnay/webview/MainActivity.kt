package com.nujnay.webview

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.webkit.*
import kotlinx.android.synthetic.main.activity_main.*
import org.apache.commons.io.IOUtils
import java.util.concurrent.TimeUnit

class MainActivity : Activity() {

    private var gotoUrl: String? = null
    private var coockieUrl: String? = null
    var jsInjectedString: String? = null


    private var needInputEmail: Boolean? = true
    private var emailOld: String? = "nujnai@outlook.com"
    private var emailOldPassword: String? = "1121firstday"

    private var emailNew: String? = null
    private var emailNewPassword: String? = null


    class InjectedScriptInterface {
        @JavascriptInterface
        fun getGmailAccount(account: String) {
            Log.d("ccdddddddd", account)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gotoUrl = "https://outlook.live.com/mail/inbox"
        coockieUrl = "https://outlook.live.com/owa/sessiondata.ashx?app=Mini"
        if (needInputEmail!!) {
            var input = IOUtils.toString(this@MainActivity.assets.open("http/hotmail_email_input.js"), "UTF-8")
            jsInjectedString = input.replace("emailcontent", emailOld!!, false)
            Log.d("jsInjectedString", jsInjectedString)
        } else {
            jsInjectedString = IOUtils.toString(this@MainActivity.assets.open("http/hotmail_email.js"), "UTF-8")
        }

        val webviewSettings: WebSettings = wv_injected.settings
        webviewSettings.domStorageEnabled = true
        webviewSettings.databaseEnabled = true
        webviewSettings.allowFileAccessFromFileURLs = true
        webviewSettings.allowUniversalAccessFromFileURLs = true
        webviewSettings.allowFileAccess = true
        webviewSettings.javaScriptEnabled = true
        webviewSettings.javaScriptCanOpenWindowsAutomatically = true
        webviewSettings.setSupportZoom(true)
        webviewSettings.builtInZoomControls = true
        webviewSettings.useWideViewPort = true
        webviewSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webviewSettings.loadWithOverviewMode = true
        webviewSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webviewSettings.pluginState = WebSettings.PluginState.ON
        webviewSettings.setSupportMultipleWindows(true)
        wv_injected.addJavascriptInterface(InjectedScriptInterface(), "getGmailAccount")
        wv_injected.webViewClient = MyWebviewClient()
        wv_injected.loadUrl(gotoUrl)
    }

    inner class MyWebviewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            try {
                wv_injected.loadUrl("javascript:$jsInjectedString")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
            runOnUiThread {
                try {
                    wv_injected.loadUrl("javascript:$jsInjectedString")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return super.shouldInterceptRequest(view, request)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (wv_injected.canGoBack()) {
                wv_injected.goBack()
            } else {
                finish()
            }
        }
        return true
    }
}