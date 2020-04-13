package com.revolgenx.anilib.video

import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView

interface PlayerManager {
    var mPlayerView: PlayerView?
    var currentPlayer: SimpleExoPlayer?
    val exoInstance: ExoVideoInstance


    fun init()
    fun prepare(resetPosition: Boolean, resetState: Boolean)
    fun pause()
    fun play(seekPosition: Long?= null)
    fun stop()
    fun fullScreen()
    fun setVolume(volume: Float)
    fun releasePlayer()
    fun attach(playerView: PlayerView)
    fun detach()
    fun isPopupSame(): Boolean
    fun currentPosition():Long
    fun duration():Long
}