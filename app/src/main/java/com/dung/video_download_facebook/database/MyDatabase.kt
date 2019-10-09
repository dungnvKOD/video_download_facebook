package com.dung.video_download_facebook.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dung.video_download_facebook.conmon.Constant

class MyDatabase(context: Context) :
    SQLiteOpenHelper(context, Constant.DATA_BASE, null, Constant.VERSION) {


    private val tbDetailVideo = "CREATE TABLE ${Constant.VIDEO}(" +
            "${Constant.NAME_VIDEO} TEXT PRIMARY KEY ," +
            "${Constant.DATAS} TEXT," +
            "${Constant.SIZES} TEXT ," +
            "${Constant.PATHS} TEXT," +
            "${Constant.BITRATE} TEXT," +
            "${Constant.FRAMES} TEXT," +
            "${Constant.DURATION} TEXT," +
            "${Constant.TYPE} TEXT," +
            "${Constant.RESOLUTION} TEXT," +
            "${Constant.THUMB} TEXT)"


    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(tbDetailVideo)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db!!.execSQL("DROP TABLE IF EXISTS ${Constant.VIDEO}")
        onCreate(db)
    }

    fun open(): SQLiteDatabase {
        return writableDatabase
    }

}