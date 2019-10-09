package com.dung.video_download_facebook.download

import android.content.Context
import android.os.Environment

import android.util.Log
import com.downloader.*
import com.dung.video_download_facebook.conmon.Constant
import com.dung.video_download_facebook.conmon.RxBus
import com.dung.video_download_facebook.events.Process
import com.dung.video_download_facebook.task.LoadVideo
import java.io.File


class DownloadManger(
    private val
    context: Context, private val url: String,
    private val downloadListener: DownloadListener

) {

    private var loadVideo: LoadVideo = LoadVideo(context)

    init {
        download()
    }

    companion object {
        val TAG = "DownloadManger"
    }


    fun updateProcess(process: Int) {
        downloadListener.onUpdateProgress(process)
    }

    fun download() {
        // lấy đường dẫn file để lưu
        val downloadPath: String =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + Constant.FOLDER_VIDEO
        val downloadFileName: String = System.currentTimeMillis().toString() + ".mp4"
        Log.d(TAG, "$ \n $downloadPath/$downloadFileName")
        val config = PRDownloaderConfig.newBuilder()
            .setReadTimeout(30000)
            .setConnectTimeout(30000)
            .build()

        PRDownloader.initialize(context, config)

        PRDownloader.download(url, downloadPath, downloadFileName)
            .build()
            .setOnStartOrResumeListener {

            }
            .setOnPauseListener {

            }
            .setOnCancelListener {

            }
            .setOnProgressListener { it: Progress ->

                //                Log.d(TAG, "process : $it")
                val currentBytes = it.currentBytes
                val totalBytes = it.totalBytes
                val progress = (currentBytes * 100L / totalBytes).toInt()
                updateProcess(progress)
                RxBus.publish(Process(progress, downloadFileName, url))
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    Log.d(TAG, "ok...")
                    loadVideo.getData(downloadPath, downloadFileName)
                    downloadListener.onSuccess()
                }

                override fun onError(error: Error?) {
                    Log.d(TAG, "onError...")
                    downloadListener.onFailedd()
                }

            })

    }

}