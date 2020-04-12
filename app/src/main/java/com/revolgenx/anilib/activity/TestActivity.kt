package com.revolgenx.anilib.activity

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.PopupWindow
import com.google.android.exoplayer2.ui.PlayerView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.fragment.test.TestFragment
import com.revolgenx.anilib.video.ExoVideoInstance
import com.revolgenx.anilib.video.PlayerManager
import com.revolgenx.anilib.video.PlayerManagerImpl
import kotlinx.android.synthetic.main.popup_video_layout.view.*
import kotlinx.android.synthetic.main.test_activty_layout.*


class TestActivity : BaseDynamicActivity() {
    override val layoutRes: Int = R.layout.test_activty_layout

    private var popupSize = PopupSize.LARGE
    private var params: WindowManager.LayoutParams? = null
    private var container: FrameLayout? = null
    private var popupWindow: PopupWindow? = null
    private var popupParams = 0

    private var playerManager: PlayerManager? = null
    private var popupPlayer: PlayerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ExoVideoInstance.getInstance().url =
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

        supportFragmentManager.beginTransaction()
            .replace(R.id.testRootLayout, TestFragment.newInstance(), "test tag").commitNow()
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
        if (playerManager == null)
            playerManager = PlayerManagerImpl(context)
        showVideoPopUp()
        playerManager?.play()
    }

    private fun showVideoPopUp() {
        container = FrameLayout(this)
        val view = layoutInflater.inflate(R.layout.popup_video_layout, container)
        popupPlayer = view.popupPlayerView
        playerManager?.attach(popupPlayer!!)
        popupParams = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        setPopupSize(0, 0);
        popupWindow = PopupWindow(container, params!!.width, params!!.height)
        popupWindow!!.isTouchable = true
        popupWindow!!.showAtLocation(testRootLayout, Gravity.TOP, 0, 0)
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
            popupWindow?.dismiss()
            popupWindow = null
            container = null
        }
    }

    override fun onBackPressed() {
        if (popupWindow?.isShowing == true) {
            playerManager?.stop()
            removePopUpView()
        } else {
            super.onBackPressed()
        }
    }

    override fun onStop() {
        playerManager?.pause()
        super.onStop()
    }


    override fun onDestroy() {
        removePopUpView()
        playerManager?.releasePlayer()
        super.onDestroy()
    }
}