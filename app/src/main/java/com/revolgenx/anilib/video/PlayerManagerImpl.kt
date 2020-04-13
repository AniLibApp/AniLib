package com.revolgenx.anilib.video

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.revolgenx.anilib.R

class PlayerManagerImpl(private val context: Context) : PlayerManager {

    override val exoInstance: ExoVideoInstance
        get() = ExoVideoInstance.getInstance()

    override var mPlayerView: PlayerView? = null

    private val mediaDataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, context.getString(R.string.app_name))
        )
    }

    override var currentPlayer: SimpleExoPlayer? = null
        get() {
            return exoInstance.exoPlayer
        }
        set(value) {
            field = value
            exoInstance.exoPlayer = value
        }

    var url: String? = null

    override fun init() {
        if (currentPlayer != null) return
        currentPlayer = SimpleExoPlayer.Builder(context).build()
        currentPlayer!!.playWhenReady = true
    }

    override fun prepare(resetPosition: Boolean, resetState: Boolean) {
        if (url != exoInstance.url) {
            url = exoInstance.url
        }

        val extractorsFactory = DefaultExtractorsFactory()
        val mediaSource: MediaSource =
            ProgressiveMediaSource.Factory(mediaDataSourceFactory, extractorsFactory)
                .createMediaSource(Uri.parse(exoInstance.url))
        currentPlayer!!.prepare(mediaSource, resetPosition, resetState)
    }

    var playWhenReady: Boolean
        get() = if (currentPlayer == null) false else currentPlayer!!.playWhenReady
        set(value) {
            if (currentPlayer == null) return
            currentPlayer!!.playWhenReady = value
        }


    override fun isPopupSame(): Boolean {
        return url == exoInstance.url && mPlayerView != null
    }

    override fun play(seekPosition: Long?) {
        if (mPlayerView != null)
            currentPlayer?.playWhenReady = true

        if (seekPosition != null) {
            if (seekPosition > 0) currentPlayer?.seekTo(seekPosition)
        }
    }

    override fun pause() {
        currentPlayer?.playWhenReady = false
    }

    override fun stop() {
        currentPlayer?.stop()
    }

    override fun setVolume(volume: Float) {
        currentPlayer?.volume = volume
    }

    override fun fullScreen() {
        if (currentPlayer == null) return
        exoInstance.seekPosition = currentPlayer!!.currentPosition
        exoInstance.isPlaying = currentPlayer!!.playWhenReady
    }

    override fun releasePlayer() {
        detach()
        currentPlayer?.release()
        currentPlayer = null
    }

    override fun currentPosition(): Long {
        return currentPlayer?.currentPosition ?: 0
    }

    override fun duration(): Long {
        return currentPlayer?.duration ?: 0
    }

    override fun attach(playerView: PlayerView) {
        mPlayerView = playerView
        mPlayerView!!.player = currentPlayer
    }

    override fun detach() {
        url = null
        mPlayerView?.player = null
        mPlayerView = null
    }

}

