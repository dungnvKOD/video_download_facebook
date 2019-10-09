package com.dung.video_download_facebook.download

import android.app.Notification
import androidx.core.app.NotificationCompat
import android.app.NotificationManager
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.dung.video_download_facebook.conmon.Constant
import androidx.core.content.ContextCompat.getSystemService
import android.annotation.TargetApi
import androidx.annotation.NonNull
import com.dung.video_download_facebook.R


class DownloadListener {
    private lateinit var downloadService: DownloadService


    fun setDownloadService(downloadService: DownloadService) {
        this.downloadService = downloadService
    }

    fun getDownloadService(): DownloadService {
        return downloadService
    }

    /**
     *
     *
     */

    fun onSuccess() {
        sendDownloadNotification("Download Success", -1)

    }

    fun onFailedd() {
        sendDownloadNotification("Download Failed", -1)
    }

    fun onUpdateProgress(progress: Int) {

        sendDownloadNotification("Downloading...", progress)
    }

    /**
     *
     *
     */

    @NonNull
    @TargetApi(26)
    @Synchronized
    private fun createChannel(): String {
        val mNotificationManager: NotificationManager? =
            downloadService.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val name = "snap map fake location "
        val importance = NotificationManager.IMPORTANCE_LOW

        val mChannel = NotificationChannel(Constant.CHANNEL_ID, name, importance)

        mChannel.enableLights(true)
        mChannel.lightColor = Color.BLUE
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel)
        } else {
//            stopSelf()

            //stopSelf sẻvice
        }
        return Constant.CHANNEL_ID
    }


    fun sendDownloadNotification(title: String, progress: Int) {
        val notificationManager =
            downloadService.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constant.CHANNEL_ID,
                "Endless Service notifications channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notification = getDownloadNotification(title, progress)
        notificationManager.notify(Constant.NOTIFICATION_ID, notification)
    }

    fun getDownloadNotification(title: String, progress: Int): Notification {
        // tạo đối tượng Notification
        val channel: String

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            channel = createChannel()
        else {
            channel = ""
        }
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(downloadService, channel)
        // tao intent
        val intent = Intent()
        val pendingIntent =
            PendingIntent.getActivity(downloadService, Constant.NOTIFICATION_REQEST_CODE, intent, 0)
        notification.setContentIntent(pendingIntent)
//        notification.setFullScreenIntent(pendingIntent, true)

        notification.setSmallIcon(R.drawable.ic_download)

        notification.setContentTitle(title)
        if (progress in 1..99) {
            val download: String = "Download progress $progress %"
            notification.setContentText(download)
            notification.setProgress(100, progress, false)
        }

        return notification.build()
    }
}

