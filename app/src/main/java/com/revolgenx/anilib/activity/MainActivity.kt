package com.revolgenx.anilib.activity

import android.os.Bundle
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.AppController
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.fragment.*
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.fragment.base.BasePagerFragment
import com.revolgenx.anilib.util.makePagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : DynamicSystemActivity() {

    companion object {
        private const val DISCOVER_POS = 0
        private const val SEASON_POS = 1
        private const val COLLECTION_POS = 2
        private const val DOWNLOAD_POS = 3
        private const val PROFILE_POS = 4
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityMainContainer.setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)
        bottomNav.setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)
        statusBarColor = statusBarColor

        noSwipeViewPager.adapter = makePagerAdapter(
            BaseFragment.newInstances(
                listOf(
                    DiscoverFragment::class.java,
                    SeasonFragment::class.java,
                    CollectionFragment::class.java,
                    DownloadFragment::class.java,
                    SettingFragment::class.java
                )
            )
        )

        themeBottomNavigation()
        noSwipeViewPager.offscreenPageLimit = 4

        bottomNav.setOnNavigationItemSelectedListener {
            val position = when (it.itemId) {
                R.id.discoverMenu -> 0
                R.id.seasonMenu -> 1
                R.id.collectionMenu -> 2
                R.id.downloadMenu -> 3
                R.id.profileMenu -> 4
                else -> 0
            }
            if (position in 0..4) {
                noSwipeViewPager.setCurrentItem(position, false)
                true
            } else
                false
        }
    }

    override fun setStatusBarColor(color: Int) {
        super.setStatusBarColor(color)
        setWindowStatusBarColor(statusBarColor);
    }

    override fun setNavigationBarTheme(): Boolean {
        return AppController.instance.isThemeNavigationBar
    }

    private fun themeBottomNavigation() {
        bottomNav.color = DynamicTheme.getInstance().get().primaryColor
        bottomNav.textColor = DynamicTheme.getInstance().get().accentColor
    }

}
