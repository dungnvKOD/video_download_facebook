package com.dung.video_download_facebook.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.dung.video_download_facebook.R
import com.dung.video_download_facebook.model.DetailVideo
import kotlinx.android.synthetic.main.dialog_detail_video.*

class DialogDetailVideo(activity: Activity, private val detailVideo: DetailVideo) :
    Dialog(activity), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_detail_video)
        setCanceledOnTouchOutside(false)

        iniView()
    }

    @SuppressLint("SetTextI18n")
    private fun iniView() {
        txt_date.text = detailVideo.dates
        txt_size.text = detailVideo.sizes.toString() + "MB"
        txt_bitrate.text = detailVideo.bitrate
        txt_path.text = detailVideo.paths
        txt_frames.text = detailVideo.frames
        txt_type.text = detailVideo.type
        txt_resolution.text = detailVideo.resolution
        txtName.text = detailVideo.nameVideo


        btn_close_detail_video.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_close_detail_video -> {


                cancel()

            }
        }
    }
}