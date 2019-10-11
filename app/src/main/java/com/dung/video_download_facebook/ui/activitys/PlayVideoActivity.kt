package com.dung.video_download_facebook.ui.activitys

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ListAdapter
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.dung.video_download_facebook.R
import com.dung.video_download_facebook.Utils
import com.dung.video_download_facebook.conmon.Constant
import com.dung.video_download_facebook.database.VideoData
import com.dung.video_download_facebook.model.DetailVideo
import com.dung.video_download_facebook.ui.adapter.ListVideoAdapter
import com.dung.video_download_facebook.ui.dialog.DialogDeleteVideo
import com.dung.video_download_facebook.ui.dialog.DialogDetailVideo
import kotlinx.android.synthetic.main.activity_play_video.*
import kotlinx.android.synthetic.main.fragment_list_video_history.*
import org.greenrobot.eventbus.EventBus
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class PlayVideoActivity : AppCompatActivity(), ListVideoAdapter.OnClickLinstener {


    companion object {

        val TAG = "PlayVideoActivity"
    }

    private var videos: ArrayList<DetailVideo> = ArrayList()
    private var position: Int = -1
    private var checkClick = true
    private var handler = Handler()
    private lateinit var mRunnable: Runnable
    private var time = 0
    private var valueSeekTo = 0
    private lateinit var database: VideoData
    private var timStop: Int = 0
    private lateinit var detailVideoPlay: DetailVideo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)
        initView()
    }

    private fun initView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.colorAccent)
            window.statusBarColor = resources.getColor(R.color.colorBackGround)
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        val bundle = intent.extras
        if (bundle!!.getBoolean(Constant.PLAY_VIDEO_URL)) {
            val url = bundle.getString(Constant.URL)
            Log.d(TAG, "dung : $url")
            playVidepUrl(url!!)

        } else {
            position = bundle!!.getInt(Constant.POSITION)
            val path = bundle.getString(Constant.PATH)
            val nameVideo = bundle.getString(Constant.NAME_VIDEO)
            val duration = bundle.getString(Constant.DURATION)
            val bitrate = bundle.getString(Constant.BITRATE)
            val dates = bundle.getString(Constant.DATE)
            val thumb = bundle.getString(Constant.THUMB)
            val type = bundle.getString(Constant.TYPE)
            val frames = bundle.getString(Constant.FRAMES)
            val sizes = bundle.getLong(Constant.SIZES)
            val resolution = bundle.getString(Constant.RESOLUTION)
            val detailVideo = DetailVideo(
                dates,
                sizes,
                path,
                bitrate,
                frames,
                nameVideo,
                duration,
                type,
                resolution,
                thumb
            )

            Log.d(TAG, "dung : $duration")
            playVideoHistory(detailVideo, position)

        }

        rlViewTouch.setOnClickListener {
            if (!checkClick) {
                imbNext.visibility = View.VISIBLE
                imbPrer.visibility = View.VISIBLE
                rcVideo.visibility = View.VISIBLE
                if (videoView.isPlaying) {
                    btnPlay.visibility = View.VISIBLE
                } else {
                    btnStop.visibility = View.VISIBLE
                }
                checkClick = true
            } else {
                btnPlay.visibility = View.INVISIBLE
                btnStop.visibility = View.INVISIBLE
                imbNext.visibility = View.INVISIBLE
                imbPrer.visibility = View.INVISIBLE
                rcVideo.visibility = View.INVISIBLE
                if (videoView.isPlaying) {
                    btnPlay.visibility = View.INVISIBLE
                } else {
                    btnStop.visibility = View.INVISIBLE
                }
                checkClick = false
            }
        }

        videoView.setOnCompletionListener {
            preVideoHistory()


        }

    }

    private fun playVidepUrl(url: String) {
        videoView.setVideoPath(url)
        videoView.requestFocus()
        videoView.start()
        sbVideo(getDurationUrl(url))
        listeners()
        imgDetail.visibility = View.GONE

    }

    private fun playVideoHistory(detailVideo: DetailVideo, position: Int) {
        detailVideoPlay = detailVideo
        database = VideoData(this)
        val uri = Uri.parse("${detailVideo.paths}/${detailVideo.nameVideo}")
        videoView.requestFocus()
        videoView.setVideoURI(uri)
        videoView.start()
        sbVideo(detailVideo.duration!!.toInt())
        listeners()


        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rcVideo.layoutManager = linearLayoutManager
        val adapter = ListVideoAdapter(this, videos, "PLAY_VIDEO")
        rcVideo.adapter = adapter
        adapter.setOnClickListener(this)

        videos = database.getAllVideo()
//        videos.sortedWith(compareBy {
//            it.nameVideo!!.substring(0, it.nameVideo!!.lastIndexOf(".")).toLong()
//        })
        adapter.insertAllVideo(videos)
        rcVideo.scrollToPosition(position)
        adapter.setOnClickListener(this)


        imgDetail.setOnClickListener {
            DialogDetailVideo(this, detailVideo).show()
        }
    }

    private fun getDurationUrl(link: String): Int {
        val media = MediaPlayer()
        try {
            media.reset()
            media.setDataSource(link)
            media.prepare()
        } catch (e: Exception) {
            Log.d("TAG", "${e.message}")
        }
        return media.duration
    }


    @SuppressLint("SetTextI18n")
    private fun sbVideo(durations: Int) {

        var currentPosition: Int?
        var isTrackTouch = false

        mRunnable = Runnable {
            currentPosition = videoView.currentPosition
            val progress = currentPosition!! * 1000 / durations
            val times = (currentPosition!! / 1000)
            time = durations / 1000 - times
            if (!isTrackTouch) {
                sbVideo.progress = progress
            }
            if (time == 0) {
                if (prLoadApi.visibility == View.VISIBLE) {
                    progressVisible()
                } else {
                    stopVisible()
                }
            }

            txtTimes.text = Utils.formatTime(time)
            handler.postDelayed(mRunnable, 1)
        }

        handler.postDelayed(
            mRunnable,
            1000
        )

        sbVideo.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var progressValues = 0

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                progressValues = p1

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                isTrackTouch = true
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                isTrackTouch = false
                valueSeekTo = (progressValues * durations) / p0!!.max
                videoView.seekTo(valueSeekTo)
            }
        })


    }


    override fun onDetailVideo(position: Int, detailVideo: DetailVideo) {
        if (detailVideoPlay.nameVideo == detailVideo.nameVideo) {
            DialogDetailVideo(this, detailVideo).show()
        } else {
            DialogDeleteVideo(this, detailVideo, position).show()
        }

    }

    override fun onPlayVideo(position: Int, detailVideo: DetailVideo) {

        this.position = position
        val uri = Uri.parse("${detailVideo.paths}/${detailVideo.nameVideo}")
        videoView.requestFocus()
        videoView.setVideoURI(uri)
        videoView.start()
        sbVideo(detailVideo.duration!!.toInt())
        listeners()
    }

    private fun listeners() {
        imgBack.setOnClickListener {
            onBackPressed()
        }
        btnPlay.setOnClickListener {
            videoView.pause()
            stopVisible()
        }

        btnStop.setOnClickListener {
            playVisible()
            videoView.start()
        }


        imbNext.setOnClickListener {
            preVideoHistory()
        }
        imbPrer.setOnClickListener {
            nextVideoHistory()
        }

        videoView.setOnInfoListener { _, p1, _ ->
            if (p1 == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                imgThumb.visibility = View.GONE
                viewBackground.visibility = View.GONE
                playVisible()
            }
            false
        }
    }


    private fun nextVideoHistory() {
        if (position + 1 <= (videos.size - 1)) {
            val p = position + 1
            position = p
            val detailVideo = this.videos[position]
            rcVideo.scrollToPosition(position)
            playVideoHistory(detailVideo, detailVideo.duration!!.toInt())

        }
    }

    private fun preVideoHistory() {
        if ((position - 1) >= 0) {
            val p = position - 1
            position = p
            val detailVideo = videos[position]
            rcVideo.scrollToPosition(position)
            playVideoHistory(detailVideo, detailVideo.duration!!.toInt())

        }
    }


    private fun progressVisible() {
        btnStop.visibility = View.GONE
        btnPlay.visibility = View.GONE
        prLoadApi.visibility = View.VISIBLE
        imbNext.visibility = View.GONE
        imbPrer.visibility = View.GONE
    }

    private fun playVisible() {
        btnPlay.visibility = View.VISIBLE
        btnStop.visibility = View.GONE
        prLoadApi.visibility = View.GONE
        imbNext.visibility = View.VISIBLE
        imbPrer.visibility = View.VISIBLE

    }

    private fun stopVisible() {
        btnPlay.visibility = View.GONE
        btnStop.visibility = View.VISIBLE
        prLoadApi.visibility = View.GONE
        imbNext.visibility = View.VISIBLE
        imbPrer.visibility = View.VISIBLE
    }


    override fun onPause() {
        videoView.pause()
        timStop = videoView.currentPosition
        checkMaxVideo()
        super.onPause()
    }

    private fun checkMaxVideo() {
        if (prLoadApi.visibility == View.VISIBLE) {
            progressVisible()
        } else {
            playVisible()
        }
    }

    override fun onResume() {
        super.onResume()
        videoView.seekTo(timStop)
        videoView.start()
    }
}
