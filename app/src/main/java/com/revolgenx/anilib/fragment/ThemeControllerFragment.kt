package com.revolgenx.anilib.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.pranavpandey.android.dynamic.support.setting.DynamicColorPreference
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils
import com.pranavpandey.android.dynamic.utils.DynamicWindowUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.fragment.base.BaseToolbarFragment
import kotlinx.android.synthetic.main.theme_controller_fragment.*

class ThemeControllerFragment : BaseToolbarFragment() {

    private var mAppThemeDay: DynamicColorPreference? = null
    private var mAppThemeNight: DynamicColorPreference? = null

    override val title: String by lazy {
        getString(R.string.dynamic_theme)
    }

    override val contentRes: Int by lazy {
        R.layout.theme_controller_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                parentFragmentManager.popBackStack()
                true
            }
            else -> false
        }
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