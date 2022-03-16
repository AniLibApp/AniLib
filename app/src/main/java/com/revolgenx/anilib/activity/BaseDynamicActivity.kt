package com.revolgenx.anilib.activity

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.app.theme.dynamicPrimaryColor
import com.revolgenx.anilib.common.event.*
import com.revolgenx.anilib.common.preference.getApplicationLocale
import com.revolgenx.anilib.common.ui.fragment.BaseFragment
import com.revolgenx.anilib.ui.view.hideKeyboard
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.EventBusListener
import com.revolgenx.anilib.util.openLink
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.math.abs


abstract class BaseDynamicActivity<T : ViewBinding> : DynamicSystemActivity(), EventBusListener {

    private var _binding: T? = null
    protected val binding: T get() = _binding!!
    fun isBindingEmpty() = _binding == null
    private var downX = 0
    abstract fun bindView(inflater: LayoutInflater, parent: ViewGroup? = null): T


    override fun getLocale(): Locale? {
        return Locale(getApplicationLocale())
    }

    lateinit var rootLayout: View


    override fun getContentView(): View {
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = bindView(layoutInflater)
        super.onCreate(savedInstanceState)
        contentView = binding.root
        rootLayout = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        rootLayout.setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        statusBarColor = DynamicColorUtils.setAlpha(dynamicBackgroundColor, 0)
    }

    override fun setStatusBarColor(@ColorInt color: Int) {
        super.setStatusBarColor(color)
        setWindowStatusBarColor(statusBarColor)
    }


    override fun onStart() {
        super.onStart()
        registerForEvent()
    }


    override fun onStop() {
        unRegisterForEvent()
        super.onStop()
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBaseEvent(event: CommonEvent) {
        when (event) {
            is OpenImageEvent -> {
                event.url?.let {
                    SimpleDraweeViewerActivity.openActivity(this, it)
                } ?: makeToast(R.string.invalid)
            }
            is OpenAlSiteEvent -> {
                openLink(getString(R.string.site_url))
            }
        }
    }


    private fun addFragmentToContainer(
        baseFragment: BaseFragment,
        transactionAnimation: FragmentAnimationType = FragmentAnimationType.FADE,
        containerId: Int
    ) {
        getTransactionWithAnimation(transactionAnimation)
            .add(containerId, baseFragment)
            .addToBackStack(null).commit()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            downX = event.rawX.toInt()
        }
        if (event.action == MotionEvent.ACTION_UP) {
            val v = currentFocus
            if (v is EditText) {
                val x = event.rawX.toInt()
                val y = event.rawY.toInt()
                //Was it a scroll - If skip all
                if (abs(downX - x) > 5) {
                    return super.dispatchTouchEvent(event)
                }
                val reducePx = 25
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                //Bounding box is to big, reduce it just a little bit
                outRect.inset(reducePx, reducePx)
                if (!outRect.contains(x, y)) {
                    v.clearFocus()
                    var touchTargetIsEditText = false
                    //Check if another editText has been touched
                    for (vi in v.getRootView().touchables) {
                        if (vi is EditText) {
                            val clickedViewRect = Rect()
                            vi.getGlobalVisibleRect(clickedViewRect)
                            //Bounding box is to big, reduce it just a little bit
                            clickedViewRect.inset(reducePx, reducePx)
                            if (clickedViewRect.contains(x, y)) {
                                touchTargetIsEditText = true
                                break
                            }
                        }
                    }
                    if (!touchTargetIsEditText) {
                        v.hideKeyboard()
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }


    protected fun getTransactionWithAnimation(transactionAnimation: FragmentAnimationType): FragmentTransaction {
        return supportFragmentManager.beginTransaction().apply {
            when (transactionAnimation) {
                FragmentAnimationType.SLIDE -> {
                    setCustomAnimations(
                        R.anim.slide_in_up,
                        R.anim.slide_out_down,
                        R.anim.slide_in_up,
                        R.anim.slide_out_down
                    )
                }
                FragmentAnimationType.FADE -> {
                    setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }

        }
            .setReorderingAllowed(true)
    }

    enum class FragmentAnimationType {
        SLIDE, FADE
    }


}