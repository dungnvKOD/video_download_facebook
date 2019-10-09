package com.dung.video_download_facebook

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

object Utils {


    @SuppressLint("SimpleDateFormat")
    fun sdf(): SimpleDateFormat {
        return SimpleDateFormat("yyyy/MM/dd HH:mm")
    }
}