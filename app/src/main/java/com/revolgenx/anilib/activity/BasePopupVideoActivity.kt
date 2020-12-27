package com.revolgenx.anilib.activity

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.viewbinding.ViewBinding
import com.google.android.exoplayer2.ui.PlayerView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.openLink
import com.revolgenx.anilib.video.ExoVideoInstance
import com.revolgenx.anilib.video.PlayerManager
import com.revolgenx.anilib.video.PlayerManagerImpl
import kotlinx.android.synthetic.main.popup_video_controller_layout.view.*
import kotlinx.android.synthetic.main.popup_video_layout.view.*


abstract class BasePopupVideoActivity<T:ViewBinding> : BaseDynamicActivity<T>() {
//    override val layoutRes: Int = R.layout.test_activty_layout

    private var popupSize = PopupSize.LARGE
    private var params: WindowManager.LayoutParams? = null
    private var container: FrameLayout? = null
    private var popupWindow: PopupWindow? = null
    private var popupParams = 0
    private var isRotating = false

    private val playerManager: PlayerManager by lazy {
        PlayerManagerImpl(this)
    }
    private var popupPlayer: PlayerView? = null

    companion object {
        val testvid =
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    }

    private fun changePopupSize() {
        if (popupWindow?.isShowing == false) return
        if (params != null)
            setPopupSize(params!!.x, params!!.y);
        else
            setPopupSize(0, 0)
        popupWindow?.update(params!!.width, params!!.height)
    }


    fun prepare() {
        if (playerManager.isPopupSame() && popupWindow?.isShowing == true) return
        showVideoPopUp()
        playerManager.play()
    }

    @Suppress("DEPRECATION")
    private fun showVideoPopUp() {
        removePopUpView()
        playerManager.init()
        playerManager.prepare(false, true)
        container = FrameLayout(this)
        val view = layoutInflater.inflate(R.layout.popup_video_layout, container)
        popupPlayer = view.popupPlayerView
        popupPlayer!!.exoFullScreenButton.setOnClickListener {
            playerManager.fullScreen()
            onMoving()
            startActivity(Intent(this, ExoVideoPlayerActivity::class.java))
        }
        popupPlayer!!.exoScreenSize.setOnClickListener {
            changePopupSize()
        }
        popupPlayer!!.exoPopupExitIv.setOnClickListener {
            playerManager.stop()
            playerManager.detach()
            removePopUpView()
        }
        popupPlayer!!.exoShareIv.setOnClickListener {
            openLink(ExoVideoInstance.getInstance().url)
        }
        playerManager.attach(popupPlayer!!)
        popupParams = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        setPopupSize(0, 0);
        popupWindow = PopupWindow(container, params!!.width, params!!.height)
        popupWindow!!.isTouchable = true
        popupWindow!!.showAtLocation(rootLayout, Gravity.TOP, 0, 0)
    }

    private fun setPopupSize(x: Int, y: Int) {
        var baseSize: Int = Resources.getSystem().displayMetrics.widthPixels
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) baseSize =
            Resources.getSystem().displayMetrics.heightPixels
        when {
            popupSize === PopupSize.SMALL -> {
                popupSize = PopupSize.NORMAL
                params = WindowManager.LayoutParams(
                    baseSize / 2,
                    baseSize / (2 * 3 / 2),
                    popupParams,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
                )
                params!!.gravity = Gravity.TOP or Gravity.START
                params!!.x = x
                params!!.y = y
            }
            popupSize === PopupSize.NORMAL -> {
                popupSize = PopupSize.LARGE
                params = WindowManager.LayoutParams(
                    (baseSize / 1.6).toInt(),
                    (baseSize / (1.6 * 3 / 2)).toInt(),
                    popupParams,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
                )
                params!!.gravity = Gravity.TOP or Gravity.START
                params!!.x = x
                params!!.y = y
            }
            popupSize === PopupSize.LARGE -> {
                popupSize = PopupSize.SMALL
                params = WindowManager.LayoutParams(
                    (baseSize / 1.2).toInt(),
                    (baseSize / (1.3 * 1.5)).toInt(),
                    popupParams,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
                )
                params!!.gravity = Gravity.TOP or Gravity.START
                params!!.x = x
                params!!.y = y
            }
        }
    }


    enum class PopupSize {
        SMALL, NORMAL, LARGE
    }


    fun removePopUpView() {
        if (popupWindow?.isShowing == true) {
            popupSize = PopupSize.LARGE
            popupWindow?.dismiss()
            popupWindow = null
            container = null
        }
    }

    override fun onBackPressed() {
        if (popupWindow?.isShowing == true) {
            playerManager.stop()
            playerManager.detach()
            removePopUpView()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        playerManager.pause()
    }

    private fun onMoving() {
        playerManager.pause()
        removePopUpView()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        isRotating = true
    }

    override fun onDestroy() {
        removePopUpView()
        if (!isRotating)
            playerManager.releasePlayer()
        super.onDestroy()
    }
}