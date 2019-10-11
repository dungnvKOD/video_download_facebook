package com.dung.video_download_facebook.ui.dialog

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import com.dung.video_download_facebook.R
import com.dung.video_download_facebook.conmon.Constant
import com.dung.video_download_facebook.ui.activitys.MainActivity
import com.dung.video_download_facebook.ui.activitys.PlayVideoActivity
import kotlinx.android.synthetic.main.dialog_option_download.*

class DialogOptionalDownload(private val activity: MainActivity, private val url: String) :
    Dialog(activity), View.OnClickListener {

    private var mLastClickTime:Long = 0

    companion object {
        val TAG = "DialogOptionalDownload"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_option_download)
        setCanceledOnTouchOutside(false)

        initView()
    }

    private fun initView() {
        btn_download_video.setOnClickListener(this)
        btn_watch_video.setOnClickListener(this)
        btn_close.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_download_video -> {
                activity.startDownload(url)
                DialogDownloading(activity, url).show()
                cancel()
            }

            R.id.btn_watch_video -> {


                if (SystemClock.elapsedRealtime() - mLastClickTime >= 1500) {
                    mLastClickTime = SystemClock.elapsedRealtime()
                    val intent = Intent(activity, PlayVideoActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString(Constant.URL, url)
                    bundle.putBoolean(Constant.PLAY_VIDEO_URL, true)
                    intent.putExtras(bundle)
                    activity.startActivity(intent)
                }

            }

            R.id.btn_close -> {
                cancel()
            }
        }
    }
}