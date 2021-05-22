package com.revolgenx.anilib.ui.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pranavpandey.android.dynamic.support.setting.base.DynamicColorPreference
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.ThemeController
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.databinding.ThemeControllerFragmentBinding

class ThemeControllerFragment : BaseToolbarFragment<ThemeControllerFragmentBinding>() {

    private var mAppThemeDay: DynamicColorPreference? = null
    private var mAppThemeNight: DynamicColorPreference? = null

    override var titleRes: Int? = R.string.theme

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ThemeControllerFragmentBinding {
        return ThemeControllerFragmentBinding.inflate(inflater, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAppThemeDay = view.findViewById(R.id.pref_app_theme_day)
        mAppThemeNight = view.findViewById(R.id.pref_app_theme_night)
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