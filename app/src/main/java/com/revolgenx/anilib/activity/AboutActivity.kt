package com.revolgenx.anilib.activity

import android.os.Bundle
import com.pranavpandey.android.dynamic.support.activity.DynamicActivity
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.AppController
import com.revolgenx.anilib.app.theme.ThemeController
import com.revolgenx.anilib.ui.fragment.about.AboutFragment
import com.revolgenx.anilib.common.preference.getApplicationLocale
import java.util.*

class AboutActivity : DynamicActivity() {
    override fun getLocale(): Locale? {
        return Locale(getApplicationLocale())
    }

    override fun getThemeRes(): Int {
        return ThemeController.appStyle
    }

    override fun onCustomiseTheme() {
        ThemeController.setLocalTheme()
    }

    override fun setStatusBarColor(color: Int) {
        super.setStatusBarColor(color)
        setWindowStatusBarColor(statusBarColor);
    }

    override fun setNavigationBarTheme(): Boolean {
        return AppController.instance.isThemeNavigationBar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationClickListener(R.drawable.ic_close) {
            finish()
        }
        if (savedInstanceState == null) {
            switchFragment(AboutFragment.newInstance(0), false)
        }
    }

}