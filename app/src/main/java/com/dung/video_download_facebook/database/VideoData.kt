package com.dung.video_download_facebook.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.dung.video_download_facebook.conmon.Constant
import com.dung.video_download_facebook.model.DetailVideo

class VideoData(context: Context) {


    companion object {
        val TAG = "VideoData"
    }

    private var data: MyDatabase = MyDatabase(context)
    private var database: SQLiteDatabase? = null

    private fun open() {
        if (database == null || !database!!.isOpen) {
            database = data.open()
        }
    }

    private fun close() {
        if (database != null || database!!.isOpen) {
            database!!.close()
        }
    }

    fun insertUser(detailVideo: DetailVideo): Boolean {
        open()
        val value = ContentValues()
        value.put(Constant.NAME_VIDEO, detailVideo.nameVideo)
        value.put(Constant.DATAS, detailVideo.dates)
        value.put(Constant.SIZES, detailVideo.sizes)
        value.put(Constant.PATHS, detailVideo.paths)
        value.put(Constant.BITRATE, detailVideo.bitrate)
        value.put(Constant.FRAMES, detailVideo.frames)
        value.put(Constant.DURATION, detailVideo.duration)
        value.put(Constant.TYPE, detailVideo.type)
        value.put(Constant.RESOLUTION, detailVideo.resolution)
        value.put(Constant.THUMB, detailVideo.thumb)
        val index = database!!.insert(Constant.VIDEO, null, value)
        close()
        Log.d(TAG, index.toString())
        return index.toInt() != -1
    }

    /**
    query tim kiem user voi username = tham so truyen vao
    1: ten bang
    2 : array cot can lay du lieu
    3: ten cot dung de query
    4 : gia tri can tim
    5,6,7 : null. la cac cau lenh xap xep dieu kien...
     */

    fun getVideoByName(nameVideo: String): DetailVideo? {
        open()
        var detailVideo: DetailVideo? = null

        val sql = "SELECT *FROM ${Constant.VIDEO} WHERE ${Constant.NAME_VIDEO}='$nameVideo'"

        val cursor = database!!.rawQuery(sql, null)

        if (cursor != null && cursor.moveToFirst()) {
            val nameVideo = cursor.getString(cursor.getColumnIndex(Constant.NAME_VIDEO))
            val dates = cursor.getString(cursor.getColumnIndex(Constant.DATAS))
            val sizes = cursor.getString(cursor.getColumnIndex(Constant.SIZES))
            val paths = cursor.getString(cursor.getColumnIndex(Constant.PATHS))
            val bitrate = cursor.getString(cursor.getColumnIndex(Constant.BITRATE))
            val frames = cursor.getString(cursor.getColumnIndex(Constant.FRAMES))
            val duration = cursor.getString(cursor.getColumnIndex(Constant.DURATION))
            val type = cursor.getString(cursor.getColumnIndex(Constant.TYPE))
            val resolution = cursor.getString(cursor.getColumnIndex(Constant.RESOLUTION))
            val thumb = cursor.getString(cursor.getColumnIndex(Constant.THUMB))


            detailVideo = DetailVideo(
                dates,
                sizes.toLong(),
                paths,
                bitrate,
                frames,
                nameVideo,
                duration,
                type,
                resolution,
                thumb
            )

        }
        close()
        return detailVideo
    }

    fun getAllVideo(): ArrayList<DetailVideo> {
        open()
        var detailVideos: ArrayList<DetailVideo> = ArrayList()
        val sql = "SELECT * FROM ${Constant.VIDEO}"
        var cursor: Cursor = database!!.rawQuery(sql, null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val nameVideo = cursor.getString(cursor.getColumnIndex(Constant.NAME_VIDEO))
            val dates = cursor.getString(cursor.getColumnIndex(Constant.DATAS))
            val sizes = cursor.getString(cursor.getColumnIndex(Constant.SIZES))
            val paths = cursor.getString(cursor.getColumnIndex(Constant.PATHS))
            val bitrate = cursor.getString(cursor.getColumnIndex(Constant.BITRATE))
            val frames = cursor.getString(cursor.getColumnIndex(Constant.FRAMES))
            val duration = cursor.getString(cursor.getColumnIndex(Constant.DURATION))
            val type = cursor.getString(cursor.getColumnIndex(Constant.TYPE))
            val resolution = cursor.getString(cursor.getColumnIndex(Constant.RESOLUTION))
            val thumb = cursor.getString(cursor.getColumnIndex(Constant.THUMB))
            var detailVideo: DetailVideo?
            detailVideo = DetailVideo(
                dates,
                sizes.toLong(),
                paths,
                bitrate,
                frames,
                nameVideo,
                duration,
                type,
                resolution,
                thumb
            )
            detailVideos.add(detailVideo)

            cursor.moveToNext()
        }
        close()
        return detailVideos
    }


    fun remove(nameVideo: String): Boolean {
        open()
        val check =
            database!!.delete(Constant.VIDEO, "${Constant.NAME_VIDEO}=?", arrayOf(nameVideo))
        close()
        return check == 1
    }


}