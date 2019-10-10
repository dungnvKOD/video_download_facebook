package com.dung.video_download_facebook.ui.activitys

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.telecom.ConnectionService
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dung.video_download_facebook.download.DownloadService
import com.dung.video_download_facebook.R
import com.dung.video_download_facebook.conmon.Constant
import com.dung.video_download_facebook.download.DownloadBiner
import com.dung.video_download_facebook.ui.frags.ListVideoHistoryFragment
import com.dung.video_download_facebook.ui.frags.SettingFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import android.content.pm.PackageManager.PERMISSION_GRANTED as PERMISSION_GRANTED1

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var connection: ServiceConnection
    private var isConnectService = false
    lateinit var downloadBiner: DownloadBiner

    companion object {
        val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        createFolder()
        connectionService()
        btn_allow_permission.setOnClickListener(this)
        lt_setting.setOnClickListener(this)
        checkPermission()
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_layout, ListVideoHistoryFragment.newFragment)
            .commit()
    }

    private fun connectionService() {

        connection = @RequiresApi(Build.VERSION_CODES.M)
        object : ConnectionService(), ServiceConnection {


            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                isConnectService = true
                downloadBiner = p1 as DownloadBiner


            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                isConnectService = false

            }
        }
        val intent = Intent(this@MainActivity, DownloadService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    fun startDownload(url: String) {
        downloadBiner.startDownload(url, 0)

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_allow_permission -> {
                checkPermission()

            }

            R.id.lt_setting -> {
                addFragment(SettingFragment.newFragment)

            }
        }
    }


    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PERMISSION_GRANTED1
        ) {

            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                Constant.ID_PERMISSION
            )

            frame_layout.visibility = View.GONE
        } else {

            goneViewPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.ID_PERMISSION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermission()

            } else {

            }
        }

    }

    private fun goneViewPermission() {
        view_permission.visibility = View.GONE
        frame_layout.visibility = View.VISIBLE
    }


    fun addFragment(f: Fragment) {
        val tag = f.javaClass.name
        val fm = supportFragmentManager
        val fragment: Fragment? = fm.findFragmentByTag(tag)

        if (fragment != null) {

            val frms: ArrayList<Fragment> = fm.fragments as ArrayList<Fragment>

            for (frm: Fragment in frms) {
                fm.beginTransaction()
                    .setCustomAnimations(
                        R.anim.left_enter,
                        R.anim.left_exit,
                        R.anim.right_enter,
                        R.anim.right_exit
                    )
                    .hide(frm)
                    .commit()

            }
            fm.beginTransaction().show(f)
                .commit()
        } else {
            fm.beginTransaction()
                .setCustomAnimations(
                    R.anim.left_enter,
                    R.anim.left_exit,
                    R.anim.right_enter,
                    R.anim.right_exit
                )
                .add(R.id.frame_layout, f, tag)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        Toast.makeText(this, "ok...", Toast.LENGTH_LONG).show()
    }


    fun popBackTask() {
        supportFragmentManager.popBackStack()
    }

    private fun createFolder() {


        val pathParent =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + Constant.FOLDER_VIDEO

        Log.d(TAG, ".....${pathParent}")
        val directory = File(pathParent)
        if (!directory.exists()) {
            directory.mkdirs()
            Log.d(TAG, "create folder")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isConnectService) {
            unbindService(connection)
            isConnectService = false
        }
    }
}
