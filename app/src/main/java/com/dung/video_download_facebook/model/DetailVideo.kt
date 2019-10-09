package com.dung.video_download_facebook.model

class DetailVideo  {
    var nameVideo: String? = null
    var dates: String? = null
    var sizes: Long = 0
    var paths: String? = null
    var bitrate: String? = null
    var frames: String? = null
    var duration: String? = null
    var type: String? = null
    var resolution: String? = null
    var thumb: String? = null


    constructor(
        dates: String?,
        sizes: Long,
        paths: String?,
        bitrate: String?,
        frames: String?,
        nameVideo: String?,
        duration: String?,
        type: String?,
        resolution: String?,
        thumb: String?
    ) {
        this.dates = dates
        this.sizes = sizes
        this.paths = paths
        this.bitrate = bitrate
        this.frames = frames
        this.nameVideo = nameVideo
        this.duration = duration
        this.type = type
        this.resolution = resolution
        this.thumb = thumb

    }
}