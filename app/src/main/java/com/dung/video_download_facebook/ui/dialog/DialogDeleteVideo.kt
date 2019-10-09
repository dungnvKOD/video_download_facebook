package com.dung.video_download_facebook.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.dung.video_download_facebook.R
import com.dung.video_download_facebook.events.DeleteVideo
import com.dung.video_download_facebook.model.DetailVideo
import kotlinx.android.synthetic.main.dialog_delete_video.*
import org.greenrobot.eventbus.EventBus

class DialogDeleteVideo(
    private val activity: Activity,
    private val detailVideo: DetailVideo,
    private var position: Int
) :
    Dialog(activity), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_delete_video)
        setCanceledOnTouchOutside(false)

        btn_close.setOnClickListener(this)
        btn_delete_video.setOnClickListener(this)
        btn_detail_video.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.btn_delete_video -> {


                EventBus.getDefault().post(
                    DeleteVideo(
                        position,
                        detailVideo.paths!!,
                        detailVideo.nameVideo!!,
                        detailVideo.thumb!!
                    )
                )

                cancel()
            }

            R.id.btn_detail_video -> {
                DialogDetailVideo(activity, detailVideo).show()
                cancel()
            }


            R.id.btn_close -> {
                cancel()

            }
        }

    }

}