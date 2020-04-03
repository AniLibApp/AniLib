package com.revolgenx.anilib.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.AppController
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.event.BaseEvent
import com.revolgenx.anilib.event.BrowseMediaEvent
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.fragment.base.ParcelableFragment
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import kotlinx.android.synthetic.main.container_activity.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class ContainerActivity : DynamicSystemActivity() {

    companion object {
        const val fragmentContainerKey = "fragment_container_key"

        fun <T : BaseFragment> openActivity(
            context: Context,
            parcelableFragment: ParcelableFragment<T>,
            option: ActivityOptionsCompat? = null
        ) {
            context.startActivity(Intent(context, ContainerActivity::class.java).also {
                it.putExtra(fragmentContainerKey, parcelableFragment)
            }, option?.toBundle())
        }

    }

    override fun getLocale(): Locale? {
        return null
    }


    override fun getThemeRes(): Int {
        return ThemeController.appStyle
    }

    override fun onCustomiseTheme() {
        ThemeController.setLocalTheme()
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container_activity)
        fragmentContainer.setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)
        statusBarColor = statusBarColor
        val parcel =
            intent.getParcelableExtra<ParcelableFragment<BaseFragment>>(fragmentContainerKey)
                ?: return

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, parcel.clzz.newInstance().apply {
                this.arguments = parcel.bundle
            }).commitNow()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBaseEvent(event: BaseEvent) {

        when (event) {
            is BrowseMediaEvent -> {
                startActivity(Intent(this, MediaBrowseActivity::class.java).apply {
                    this.putExtra(MediaBrowseActivity.MEDIA_BROWSER_META, event.mediaBrowserMeta)
                })
            }
        }
    }

    override fun setStatusBarColor(color: Int) {
        super.setStatusBarColor(color)
        setWindowStatusBarColor(statusBarColor);
    }

    override fun setNavigationBarTheme(): Boolean {
        return AppController.instance.isThemeNavigationBar
    }


    override fun onStop() {
        unRegisterForEvent()
        super.onStop()
    }

}