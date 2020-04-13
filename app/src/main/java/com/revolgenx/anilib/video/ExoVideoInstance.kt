package com.revolgenx.anilib.video

import com.google.android.exoplayer2.SimpleExoPlayer

class ExoVideoInstance {
    companion object {
        private var instance: ExoVideoInstance? = null
        fun getInstance(): ExoVideoInstance {
            if (instance == null) {
                instance = ExoVideoInstance()
            }
            return instance!!
        }
    }

    var exoPlayer: SimpleExoPlayer? = null
    var seekPosition: Long = 0
    var isPlaying = false
    var url: String = ""
}