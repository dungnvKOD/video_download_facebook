package com.dung.video_download_facebook.task

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Log
import com.dung.video_download_facebook.Utils
import com.dung.video_download_facebook.conmon.Constant
import com.dung.video_download_facebook.conmon.RxBus
import com.dung.video_download_facebook.database.VideoData
import com.dung.video_download_facebook.model.DetailVideo
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.collections.ArrayList
import io.reactivex.schedulers.Schedulers
import io.reactivex.Observable
import java.util.*
import android.R.id
import com.dung.video_download_facebook.events.DeleteVideo


class LoadVideo(private val context: Context) {

    private var database: VideoData = VideoData(context)

    init {
//        RxBus.listen(DeleteVideo::class.java).subscribe {
//            val deleteFile = deleteFile(it.path, it.name, it.thumb)
//
//        }
    }

    @SuppressLint("CheckResult")
    fun getData(path: String, nameFile: String) {
        val element = File("$path/$nameFile")
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource("$path/$nameFile")
        val intBitrate = Integer.parseInt(
            retriever.extractMetadata(
                MediaMetadataRetriever
                    .METADATA_KEY_BITRATE
            )
        )

        val bitrate = (intBitrate / 1024).toString() + " kbps"
        val duration =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val mimeType =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
        val width =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
        val height =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)

        val resolution = width + "x" + height
        val size = element.length()
        val sizes = size / 1024000
        val date = element.lastModified()
        val dateFile = Utils.sdf().format(Date(date))
        Log.d("TAG_PPPPPP", "$date")
        val icon = retriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST)
        val image = saveImageName(icon, nameFile)

        val detailVideo = DetailVideo(
            dateFile, sizes, path, bitrate, "", nameFile, duration,
            mimeType, resolution, image
        )
        RxBus.publish(detailVideo)

        database.insertUser(detailVideo)

    }

    fun deleteFile(path: String, nameFile: String, thumb: String): Boolean {
        var videoDeleted: Boolean = false
        var thumbDeleted: Boolean = false
        try {
            val fileVideo = File("$path/$nameFile")
            videoDeleted = fileVideo.delete()
            val fileThumb = File(thumb)
            thumbDeleted = fileThumb.delete()
        } catch (ex: java.lang.Exception) {

        } finally {
            return videoDeleted && thumbDeleted

        }

    }


    private fun saveImageName(bitmapImage: Bitmap?, name: String): String {
        val cw = ContextWrapper(context)
        val directoryThumbnail = cw.getDir("thumbnail", Context.MODE_PRIVATE)
        if (!directoryThumbnail.exists()) {
            directoryThumbnail.mkdir()
        }
        val nameVideo = name.substring(0, name.lastIndexOf(".")) + ".jpg"
        val myPath = File(directoryThumbnail, nameVideo)
        try {
            val fos = FileOutputStream(myPath)
            bitmapImage?.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return directoryThumbnail.path + File.separator + nameVideo
    }


    @SuppressLint("CheckResult")
    private fun getAllData() {

        var listVideo: ArrayList<DetailVideo> = ArrayList()

        val pathParent =
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + File.separator + Constant.FOLDER_VIDEO
        val directory = File(pathParent)
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val files = directory.listFiles()
        for (element in files) {
            try {
                val name = element.name
                val path = pathParent + File.separator + element.name
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(path)

                val intBitrate = Integer.parseInt(
                    retriever.extractMetadata(
                        MediaMetadataRetriever
                            .METADATA_KEY_BITRATE
                    )
                )

                val bitrate = (intBitrate / 1024).toString() + " kbps"
                val duration =
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                val mimeType =
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
                val width =
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                val height =
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)

                val resolution = width + "x" + height
                val size = element.length()
                val sizes = size / 1024000
                val date = element.lastModified()
                val dateFile = Utils.sdf().format(Date(date))
                Log.d("TAG_PPPPPP", "$name")
                val icon = retriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST)
//                val frames = retriever.getFrameAtTime(10000, MediaMetadataRetriever.OPTION_CLOSEST).toString()
                val image = saveImageName(icon, name)

                val detailVideo = DetailVideo(
                    dateFile, sizes, path, bitrate, "", name, duration,
                    mimeType, resolution, image
                )
                listVideo.add(detailVideo)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        listVideo.reverse()
//        saveListDetail(listVideo)
    }


}