package com.revolgenx.anilib.video

import com.google.android.exoplayer2.ui.PlayerView

interface PlayerManager {
    fun pause()
    fun play(seekPosition: Boolean = false)
    fun stop()
    fun fullScreen()
    fun setVolume(volume: Float)
    fun releasePlayer()
    fun attach(playerView: PlayerView)
    fun detach()
}