package com.dung.video_download_facebook.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import com.dung.video_download_facebook.R
import kotlinx.android.synthetic.main.dialog_about.*

class AboutDialog(context: Context) : Dialog(context) {

    init {
        this.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_about)
        listener()
    }

    fun listener(){
        txtCloseAbout.setOnClickListener {
            dismiss()
        }

        txtCheckUpdate.setOnClickListener {
            val packageName = context.packageName
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            context.startActivity(intent)
            dismiss()
        }
    }

    fun container(): AboutDialog {
        return this
    }
}