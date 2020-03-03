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
import kotlinx.android.synthetic.main.theme_controller_fragment.*

class ThemeControllerFragment : BaseFragment() {

    private var mAppThemeDay: DynamicColorPreference? = null
    private var mAppThemeNight: DynamicColorPreference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.theme_controller_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).also {
            it.setSupportActionBar(toolbar)
            it.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            it.supportActionBar!!.title = getString(R.string.ads_dynamic_theme)
        }
        setHasOptionsMenu(true)
        mAppThemeDay = view.findViewById(R.id.pref_app_theme_day)
        mAppThemeNight = view.findViewById(R.id.pref_app_theme_night)

        if (!DynamicWindowUtils.isNavigationBarThemeSupported(requireContext())) {
            view.findViewById<View>(R.id.pref_navigation_bar_theme).visibility = View.GONE
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finishActivity()
            }
        }
        return super.onOptionsItemSelected(item)
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