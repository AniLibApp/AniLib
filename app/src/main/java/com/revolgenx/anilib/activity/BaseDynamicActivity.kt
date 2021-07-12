package com.revolgenx.anilib.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicPrimaryColor
import com.revolgenx.anilib.common.preference.getApplicationLocale
import com.revolgenx.anilib.common.ui.fragment.BaseFragment
import com.revolgenx.anilib.data.meta.*
import com.revolgenx.anilib.infrastructure.event.*
import com.revolgenx.anilib.ui.dialog.MediaViewDialog
import com.revolgenx.anilib.ui.fragment.media.MediaListingFragment
import com.revolgenx.anilib.util.EventBusListener
import com.revolgenx.anilib.util.openLink
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

abstract class BaseDynamicActivity<T : ViewBinding> : DynamicSystemActivity(), EventBusListener {

    private var _binding: T? = null
    protected val binding: T get() = _binding!!
    fun isBindingEmpty() = _binding == null

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
        statusBarColor = dynamicPrimaryColor
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
            is ImageClickedEvent -> {
                SimpleDraweeViewerActivity.openActivity(this, DraweeViewerMeta(event.meta.url))
            }

            is YoutubeClickedEvent -> {
                openLink(event.meta.url)
            }

            is VideoClickedEvent -> {
                openLink(event.videoMeta.url)
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