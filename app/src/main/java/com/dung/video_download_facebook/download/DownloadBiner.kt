package com.dung.video_download_facebook.download

import android.app.Activity
import android.content.Context
import android.os.Binder
import com.dung.video_download_facebook.conmon.Constant

class DownloadBiner(private val context: Context) : Binder() {

    private var downloadManager: DownloadManger? = null
    private val downloadListener = DownloadListener()

    fun startDownload(downloadUrl: String, process: Int) {
        downloadManager = DownloadManger(context, downloadUrl, downloadListener)
        val notification = downloadListener.getDownloadNotification("Downloading...", process)
        downloadListener.getDownloadService()
            .startForeground(Constant.NOTIFICATION_ID, notification)

    }

    fun setDownloadListstener(downloadService: DownloadService) {
        downloadListener.setDownloadService(downloadService)

    }




}