package com.revolgenx.anilib.fragment.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pranavpandey.android.dynamic.support.setting.DynamicColorPreference
import com.pranavpandey.android.dynamic.utils.DynamicWindowUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.fragment.base.BaseToolbarFragment

class ThemeControllerFragment : BaseToolbarFragment() {

    private var mAppThemeDay: DynamicColorPreference? = null
    private var mAppThemeNight: DynamicColorPreference? = null

    override val title: Int = R.string.dynamic_theme


    override val contentRes: Int by lazy {
        R.layout.theme_controller_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).also {
            it.supportActionBar?.setDisplayShowHomeEnabled(true)
            it.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        }

        mAppThemeDay = view.findViewById(R.id.pref_app_theme_day)
        mAppThemeNight = view.findViewById(R.id.pref_app_theme_night)

        if (!DynamicWindowUtils.isNavigationBarThemeSupported(requireContext())) {
            view.findViewById<View>(R.id.pref_navigation_bar_theme).visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        updatePreferences()
    }


    private fun updatePreferences() {
        if (ThemeController.isAutoTheme) {
            mAppThemeDay!!.isEnabled = true
            mAppThemeNight!!.isEnabled = true
        } else {
            mAppThemeDay!!.isEnabled = false
            mAppThemeNight!!.isEnabled = false
        }
    }
}