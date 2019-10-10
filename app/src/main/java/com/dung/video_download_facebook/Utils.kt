package com.dung.video_download_facebook

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

object Utils {


    @SuppressLint("SimpleDateFormat")
    fun sdf(): SimpleDateFormat {
        return SimpleDateFormat("yyyy/MM/dd HH:mm")
    }

    @SuppressLint("DefaultLocale")
    fun formatTime(values: Int): String {
        val hours = values / 3600
        val minutes = values / 60 - hours * 60
        val seconds = values - hours * 3600 - minutes * 60
        return if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}