package com.dung.video_download_facebook.ui.frags


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*

import com.dung.video_download_facebook.R
import com.dung.video_download_facebook.conmon.Constant
import com.dung.video_download_facebook.conmon.RxBus
import com.dung.video_download_facebook.events.Process
import com.dung.video_download_facebook.ui.activitys.MainActivity
import com.dung.video_download_facebook.ui.activitys.PlayVideoActivity
import com.dung.video_download_facebook.ui.dialog.DialogOptionalDownload
import kotlinx.android.synthetic.main.fragment_web_view.*

/**
 * A simple [Fragment] subclass.
 */
class WebViewFragment : Fragment() {

    companion object {
        val newFragment = WebViewFragment()
        val TAG = "WebViewFragment"

    }

    private var mLastClickTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webViewClient()
        listener()
//

    }


    private fun listener() {
        llBack.setOnClickListener {
            webFacebook.goBack()
        }
        llNext.setOnClickListener {
            webFacebook.goForward()
        }
        img_refresh.setOnClickListener {
            webFacebook.reload()
        }
        imbClose.setOnClickListener {
            (activity as MainActivity).popBackTask()
        }
        imbHomeFB.setOnClickListener {
            webFacebook.loadUrl(Constant.PORT1)
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    fun webViewClient() {

        webFacebook.isVerticalScrollBarEnabled = false
        webFacebook.isHorizontalScrollBarEnabled = false
        webFacebook.settings.javaScriptEnabled = true
        webFacebook.settings.pluginState = WebSettings.PluginState.ON
        webFacebook.settings.builtInZoomControls = true
        webFacebook.settings.displayZoomControls = true
        webFacebook.settings.useWideViewPort = true
        webFacebook.settings.loadWithOverviewMode = true
        webFacebook.settings.userAgentString =
            "Mozilla/5.0 (Linux; U; Android 4.1.1; en-gb; Build/KLP) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Safari/534.30"
        webFacebook.addJavascriptInterface(this, "FBDownloader")
        webFacebook.webViewClient = (object : WebViewClient() {
            override fun onLoadResource(view: WebView, url: String) {
                if (webFacebook != null) {
                    txtLinkWeb.text = view.originalUrl

                    if (view.originalUrl != null) {
                        if (webFacebook.canGoBack()) {
                            llBack.alpha = 1.0f
                        } else {
                            llBack.alpha = 0.5f
                        }
                        if (webFacebook.canGoForward()) {
                            llNext.alpha = 1.0f
                        } else {
                            llNext.alpha = 0.5f
                        }
                    }

                    webFacebook.loadUrl(
                        "javascript:(function prepareVideo() { "
                                + "var el = document.querySelectorAll('div[data-sigil]');"
                                + "for(var i=0;i<el.length; i++)"
                                + "{"
                                + "var sigil = el[i].dataset.sigil;"
                                + "if(sigil.indexOf('inlineVideo') > -1){"
                                + "delete el[i].dataset.sigil;"
                                + "var jsonData = JSON.parse(el[i].dataset.store);"
                                + "console.log(el[i].dataset.store);"
                                + "el[i].setAttribute('onClick', 'FBDownloader.processVideo(\"'+jsonData['src']+'\",\"'+jsonData['videoID']+'\");');"
                                + "}" + "}" + "})()"
                    )
                    webFacebook.loadUrl(("javascript:( window.onload=prepareVideo" + ")()"))
                }
            }

            override fun onPageFinished(view: WebView, url: String) {


            }
        })

        CookieSyncManager.createInstance(activity)
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        CookieSyncManager.getInstance().startSync()
        webFacebook.loadUrl(Constant.PORT1)
        CookieSyncManager.getInstance().stopSync()
    }

    @JavascriptInterface
    fun processVideo(vidData: String, vidID: String) {
        val url = Constant.PORT1 + vidID
        Log.d(TAG, "dung...$vidData")


        if (SystemClock.elapsedRealtime() - mLastClickTime >= 1500) {
            mLastClickTime = SystemClock.elapsedRealtime()
            DialogOptionalDownload(activity as MainActivity, vidData).show()
        }

    }
}
