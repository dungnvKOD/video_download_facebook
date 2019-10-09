package com.dung.video_download_facebook.download

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder

@SuppressLint("Registered")
class DownloadService : Service() {

    private var downloadBiner: DownloadBiner = DownloadBiner(this)

    override fun onBind(p0: Intent?): IBinder? {

        downloadBiner.setDownloadListstener(this)
        return downloadBiner
    }
}