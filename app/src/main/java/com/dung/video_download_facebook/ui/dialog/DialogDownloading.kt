package com.dung.video_download_facebook.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.util.TypedValue
import android.widget.RelativeLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dung.video_download_facebook.R
import com.dung.video_download_facebook.conmon.RxBus
import com.dung.video_download_facebook.events.Process
import kotlinx.android.synthetic.main.dialog_downloading.*

class DialogDownloading(private val activity: Activity, var url: String) : Dialog(activity) {

    companion object {
        val TAG = "DialogDownloading"
    }

    private lateinit var handler: Handler
    val what = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_downloading)
        setCanceledOnTouchOutside(false)

//            Glide.with(context)
//                .load(url)
//                .thumbnail(Glide.with(context).load(url))
//                .into(imgThumbnai)

        handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)

                if (msg.what == what) {
                    val bundle = msg.data
                    val name: String = bundle["NAME"] as String
                    txtNameFile.text = name

                    val hide = 0.6 * msg.arg1
                    updateProgress(hide.toInt())
                    if (msg.arg1 == 100) {
                        Toast.makeText(activity, "Download Success !", Toast.LENGTH_LONG).show()
                        cancel()
                    }
                    txtTextPro.text = msg.arg1.toString() + "%"
                }
            }
        }

        initView()
    }

    private fun updateProgress(hide: Int) {
        var height = convertDpToPixel(60, context)
        height -= convertDpToPixel(hide, context)
        val params = RelativeLayout.LayoutParams(convertDpToPixel(56, context), height)
        imgProgress.layoutParams = params

        Log.d("updateProgress", "$hide")

    }

    fun convertDpToPixel(dp: Int, context: Context): Int {
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        )
        return Math.round(px)
    }


    @SuppressLint("CheckResult")
    private fun initView() {

        RxBus.listen(Process::class.java).subscribe { it ->
            activity.runOnUiThread {
                //                txtTextPro.text = it.process.toString() + "%"

                val messenger: Message = Message()
                messenger.arg1 = it.process

                val bundle = Bundle()
                bundle.putString("NAME", it.name)
                bundle.putString("URL", it.url)
                bundle.putInt("PROGRESS", it.process)
                messenger.data = bundle
                messenger.what = what
                handler.sendMessage(messenger)
            }
        }

    }
}