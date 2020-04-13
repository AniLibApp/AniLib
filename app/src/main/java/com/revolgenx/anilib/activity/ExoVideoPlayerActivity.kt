package com.revolgenx.anilib.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.view.View
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.preference.orientation
import com.revolgenx.anilib.video.PlayerManager
import com.revolgenx.anilib.video.PlayerManagerImpl
import com.revolgenx.anilib.view.drawable.PlayPauseDrawable
import kotlinx.android.synthetic.main.exo_video_player_activity.*
import kotlinx.android.synthetic.main.exo_video_player_controller_layout.*
import java.util.*


class ExoVideoPlayerActivity : DynamicSystemActivity() {
    private var lastTouchTime: Long = 0
    private var playPauseDrawable = PlayPauseDrawable()
    private var fullMode = 0
    private var currentMode: Int = 0
    private var screenSize: Point? = null
    private val playerManagerImpl: PlayerManager by lazy {
        PlayerManagerImpl(this)
    }

    private var isRotation = false
    override fun getLocale(): Locale? {
        return null
    }

    override fun getThemeRes(): Int {
        return ThemeController.appStyle
    }

    override fun onCustomiseTheme() {
        ThemeController.setLocalTheme()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exo_video_player_activity)
        exoplayerRootLayout.setBackgroundColor(Color.TRANSPARENT)

        fullMode = AspectRatioFrameLayout.RESIZE_MODE_FILL;
        currentMode = AspectRatioFrameLayout.RESIZE_MODE_FIT;
        screenSize = screenSize();

        setSupportActionBar(exoVideoToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        initializePlayer()
        initController()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initializePlayer() {
        exoVideoPlayerView.setControllerVisibilityListener { visibility ->
            exoVideoToolbar.visibility = visibility
            when (visibility) {
                View.GONE -> {
                    hideSystemUi()
                }
                View.VISIBLE -> {
                    showSystemUI()
                }
            }
        }

        exoVideoPlayerView.requestFocus()
        playerManagerImpl.init()
        playerManagerImpl.attach(exoVideoPlayerView)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun initController() {
        if (playerManagerImpl.currentPlayer == null) return

        exoVideoPlayerView.apply {
            pausePlayFb.setImageDrawable(playPauseDrawable)

            if (playerManagerImpl.exoInstance.isPlaying) {
                exoVideoPlayerView.post {
                    playerManagerImpl.play()
                    playPauseDrawable.setState(PlayPauseDrawable.State.Pause)
                }
            } else {
                playPauseDrawable.setState(PlayPauseDrawable.State.Play)
            }
            pausePlayFb.setOnClickListener {
                delayHideControl();
                val state = playerManagerImpl.currentPlayer!!.playWhenReady
                if (state) {
                    playerManagerImpl.pause()
                    playPauseDrawable.setState(PlayPauseDrawable.State.Play)
                } else {
                    playerManagerImpl.play()
                    playPauseDrawable.setState(PlayPauseDrawable.State.Pause)
                }
            }

            exoSkipPreIv.setOnClickListener {
                var currentPosition = playerManagerImpl.currentPosition()
                if (currentPosition - 10000 < 0) currentPosition = 0;
                playerManagerImpl.play(currentPosition)
            }

            exoSkipPostIv.setOnClickListener {
                val currentPosition = playerManagerImpl.currentPosition()
                if (currentPosition + 10000 < playerManagerImpl.duration())
                    playerManagerImpl.play(currentPosition + 10000)
            }

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR

            exoRotationIv.setOnClickListener {
                val requestScreenInfo = requestedOrientation
                if (requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                } else if (requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
                } else if (requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_SENSOR || requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                }
            }

        }
    }

    private fun screenSize(): Point? {
        val display: Display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }

    private fun delayHideControl() {
        lastTouchTime = System.currentTimeMillis()
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    private fun hideSystemUi() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        isRotation = true
    }


    override fun onStop() {
        super.onStop()
        if (!isRotation) {
            playerManagerImpl.pause()
            playPauseDrawable.setState(PlayPauseDrawable.State.Pause)
        }
    }

    override fun onDestroy() {
        if (!isRotation) {
//            playerManagerImpl.releasePlayer()
//        } else {
            playerManagerImpl.detach()
        }

        super.onDestroy()
    }
}