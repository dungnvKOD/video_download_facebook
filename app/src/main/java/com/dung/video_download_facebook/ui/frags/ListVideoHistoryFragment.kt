package com.dung.video_download_facebook.ui.frags

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.dung.video_download_facebook.R
import com.dung.video_download_facebook.conmon.RxBus
import com.dung.video_download_facebook.database.VideoData
import com.dung.video_download_facebook.events.DeleteVideo
import com.dung.video_download_facebook.model.DetailVideo
import com.dung.video_download_facebook.ui.activitys.MainActivity
import com.dung.video_download_facebook.ui.adapter.ListVideoAdapter
import com.dung.video_download_facebook.ui.dialog.DialogDeleteVideo
import kotlinx.android.synthetic.main.fragment_list_video_history.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ListVideoHistoryFragment : Fragment(), View.OnClickListener,
    ListVideoAdapter.OnClickLinstener {
    override fun onPlayVideo(position: Int, detailVideo: DetailVideo) {

    }

    override fun onDetailVideo(position: Int, detailVideo: DetailVideo) {
        DialogDeleteVideo(
            context as Activity,
            detailVideo,
            position
        ).show()
    }


    companion object {
        val TAG = "ListVideoHistory"
        val newFragment = ListVideoHistoryFragment()
    }

    private lateinit var database: VideoData
    private lateinit var videoAdapter: ListVideoAdapter
    private lateinit var videos: ArrayList<DetailVideo>
    private lateinit var dialogDeleteVideo: DialogDeleteVideo


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_video_history, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        EventBus.getDefault().register(this)
        initView()
    }

    private fun initView() {


        videos = ArrayList()
        val linearLayoutManager = GridLayoutManager(activity!!, 3)
        rcv_video_history.layoutManager = linearLayoutManager
        videoAdapter = ListVideoAdapter(activity!!, videos, "HISTORY")
        rcv_video_history.adapter = videoAdapter
        videoAdapter.setOnClickListener(this)
        fab()
        loadData()
    }

    private fun fab() {
        btn_browser.setOnClickListener(this)
        btn_download.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_browser -> {
                (activity as MainActivity).addFragment(WebViewFragment.newFragment)
                menu_fab.close(true)
            }

            R.id.btn_download -> {
                menu_fab.close(true)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun loadData() {
        database = VideoData(activity!!)
        val videos: ArrayList<DetailVideo> = database.getAllVideo()
        if (videos.size == 0) {
            btn_how_to.visibility = View.VISIBLE
            txt_no_video.visibility = View.VISIBLE

        }
        Log.d(TAG, "number video : ${videos.size}")
        videos.sortedWith(compareBy {
            it.nameVideo!!.substring(0, it.nameVideo!!.lastIndexOf(".")).toLong()
        })
        videoAdapter.insertAllVideo(videos)


        RxBus.listen(DetailVideo::class.java).subscribe {
            btn_how_to.visibility = View.GONE
            txt_no_video.visibility = View.GONE
            videoAdapter.insertVideo(it)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: DeleteVideo) {
        database.remove(event.name)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}
