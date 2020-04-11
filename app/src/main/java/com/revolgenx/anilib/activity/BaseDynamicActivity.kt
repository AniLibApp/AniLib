package com.revolgenx.anilib.activity

import android.os.Bundle
import android.view.ViewGroup
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.controller.AppController
import com.revolgenx.anilib.controller.ThemeController
import java.util.*

abstract class BaseDynamicActivity  : DynamicSystemActivity(){

    override fun getLocale(): Locale? {
        return null
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

    abstract val layoutRes:Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        findViewById<ViewGroup>(android.R.id.content).getChildAt(0).setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)
        statusBarColor = statusBarColor
    }
}