package com.dung.video_download_facebook.ui.frags


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dung.video_download_facebook.R
import com.dung.video_download_facebook.ui.activitys.MainActivity
import com.dung.video_download_facebook.ui.dialog.AboutDialog
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment(), View.OnClickListener {


    companion object {
        val newFragment = SettingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }


    private fun initView() {
        toolbar_setting.setOnClickListener(this)

        llShare.setOnClickListener {
//            if (SystemClock.elapsedRealtime() - mLastClickTime >= 1000) {
//                mLastClickTime = SystemClock.elapsedRealtime()
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share link!"))
//            }
        }

        llAbout.setOnClickListener {
                AboutDialog(context!!).container().show()
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.toolbar_setting -> {

                (activity as MainActivity).popBackTask()
            }
        }
    }


}
