package com.nujnay.webview

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.*
import kotlinx.android.synthetic.main.activity_main.*
import org.apache.commons.io.IOUtils

class MainActivity : Activity() {
    private var gotoUrl: String? = null
    private var coockieUrl: String? = null

    public var jsInjectedOldEmailOK: Boolean? = false
    public var jsInjectedOldPwdOK: Boolean? = false

    public var jsInjectedEmailOK: Boolean? = false
    public var jsInjectedPwdOK: Boolean? = false

    public var jsInjectedOldEmail: String? = null
    public var jsInjectedOldPwd: String? = null

    public var jsInjectedGetEmail: String? = null
    public var jsInjectedGetPwd: String? = null

    public var needInputEmail: Boolean? = false
    public var emailOld: String? = "nujnai@outlook.com"
    public var emailOldPassword: String? = "1121firstday"

    public var emailNew: String? = null
    public var emailNewPassword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gotoUrl = "https://outlook.live.com/mail/inbox"
        coockieUrl = "https://outlook.live.com/owa/sessiondata.ashx?app=Mini"
        initjsInjected()
        initWebview()
    }

    private fun initjsInjected() {
        val oldEmailJS = IOUtils.toString(this@MainActivity.assets.open("deprected/hotmail_email_input.js"), "UTF-8")
        jsInjectedOldEmail = oldEmailJS.replace("emailcontent", emailOld!!, false)

        val oldPwdJS = IOUtils.toString(this@MainActivity.assets.open("deprected/hotmail_email_input.js"), "UTF-8")
        jsInjectedOldPwd = oldPwdJS.replace("pwdcontent", emailOldPassword!!, false)

        jsInjectedGetEmail = IOUtils.toString(this@MainActivity.assets.open("deprected/hotmail_email.js"), "UTF-8")
        jsInjectedGetPwd = IOUtils.toString(this@MainActivity.assets.open("deprected/hotmail_pwd.js"), "UTF-8")
    }

    fun initWebview() {
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
        WebView.setWebContentsDebuggingEnabled(true)
        wv_injected.addJavascriptInterface(InjectedScriptInterface(), "getGmailAccount")
        wv_injected.webViewClient = MyWebviewClient()
        wv_injected.loadUrl(gotoUrl)
    }

    inner class MyWebviewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            try {
                injectJs()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
            runOnUiThread {
                try {
                    injectJs()
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

    fun injectJs() {
        if (needInputEmail!!) {
            if (jsInjectedOldEmailOK!!) {// 老的email注入了 1 注入老密码 2
                wv_injected.loadUrl("javascript:$jsInjectedGetEmail")
            } else {
                wv_injected.loadUrl("javascript:$jsInjectedOldEmail")
            }
        } else {
            if (jsInjectedEmailOK!!) {
                wv_injected.loadUrl("javascript:$jsInjectedGetPwd")
            } else {
                wv_injected.loadUrl("javascript:$jsInjectedGetEmail")
            }
        }
    }

    inner class InjectedScriptInterface {
        @JavascriptInterface
        fun getGmailAccount(account: String) {

            if (account.startsWith("email:")) {
                if (account.checkEmail()) {
                    emailNew = account
                }
            }
            Log.d("getGmailAccountttt", account)
        }
    }

    fun String.checkEmail(): Boolean {
        return (this.contentEquals("@outlook.com")
                || this.contentEquals("@hotmail.com")
                || this.contentEquals("@live.com")
                || this.contentEquals("@hotmail.co")
                || this.contentEquals("@live.ie")
                || this.contentEquals("@outlook.ie"))
    }
}
