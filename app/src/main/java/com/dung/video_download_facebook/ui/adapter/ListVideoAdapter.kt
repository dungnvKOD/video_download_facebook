package com.dung.video_download_facebook.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dung.video_download_facebook.R
import com.dung.video_download_facebook.events.DeleteVideo
import com.dung.video_download_facebook.model.DetailVideo
import com.dung.video_download_facebook.ui.activitys.MainActivity
import com.dung.video_download_facebook.ui.activitys.PlayVideoActivity
import com.dung.video_download_facebook.ui.dialog.DialogDeleteVideo
import com.jcminarro.roundkornerlayout.RoundKornerRelativeLayout
import kotlinx.android.synthetic.main.item_view.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ListVideoAdapter(private var context: Context, private var videos: ArrayList<DetailVideo>) :
    RecyclerView.Adapter<ListVideoAdapter.VideoViewHodel>() {

    private val inflater = LayoutInflater.from(context)
    private lateinit var onClickLinstener: OnClickLinstener

    companion object {
        val TAG = "ListVideoAdapter"
    }

    init {
        EventBus.getDefault().register(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHodel {
        val view = inflater.inflate(R.layout.item_view, parent, false)

        return VideoViewHodel(view)

    }

    override fun getItemCount(): Int {
        Log.d(TAG, "size  : ${videos.size}")
        return videos.size
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: VideoViewHodel, position: Int) {
        val video = videos[holder.adapterPosition]
        Glide.with(context)
            .load(video.paths + "/" + video.nameVideo)
            .into(holder.img_video)

        holder.txt_time.text = SimpleDateFormat("mm:ss").format(Date(video.duration!!.toLong()))
        holder.img_detail.setOnClickListener {

            DialogDeleteVideo(context as MainActivity, video, holder.adapterPosition).show()
            Log.d(TAG, "dung  : ${holder.adapterPosition}")
        }

        holder.round_item.setOnClickListener {
            val intent = Intent(context, PlayVideoActivity::class.java)
            context.startActivity(intent)

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: DeleteVideo) {
        deleteVideo(event.position, videos[event.position])
    }

    fun deleteVideo(position: Int, video: DetailVideo) {
        val fileVideo = File(video.paths + "/" + video.nameVideo)
        fileVideo.delete()
        val fileImage = File(video.thumb)
        fileImage.delete()
        videos.removeAt(position)
        notifyItemRemoved(position)
        Toast.makeText(context, "pos : $position", Toast.LENGTH_LONG).show()
    }


    fun insertAllVideo(videosInsert: ArrayList<DetailVideo>) {
        videos.clear()
        for (i in 0 until videosInsert.size) {
            videos.add(0, videosInsert[i])
            notifyItemInserted(0)
        }
    }

    fun insertVideo(video: DetailVideo) {
        videos.add(0, video)
        notifyItemInserted(0)
    }

    inner class VideoViewHodel(view: View) : RecyclerView.ViewHolder(view) {
        val round_item: RoundKornerRelativeLayout = view.round_item
        val img_video: ImageView = view.img_video
        val txt_time: TextView = view.txt_time
        val img_detail: ImageButton = view.findViewById(R.id.img_detail)

    }

    fun setOnClickListener(onClickLinstener: OnClickLinstener) {
        this.onClickLinstener = onClickLinstener
    }

    interface OnClickLinstener {
        fun openDialog(position: Int, detailVideo: DetailVideo)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        EventBus.getDefault().unregister(this)
    }
}