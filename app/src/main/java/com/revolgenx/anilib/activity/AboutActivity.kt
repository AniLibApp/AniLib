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
    override fun getLocale(): Locale {
        return Locale(getApplicationLocale())
    }

    override fun setStatusBarColor(color: Int) {
        super.setStatusBarColor(color)
        setWindowStatusBarColor(statusBarColor);
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationClickListener {
            finish()
        }
        if (savedInstanceState == null) {
            switchFragment(AboutFragment(), false)
        }
    }

}