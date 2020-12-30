package com.revolgenx.anilib.ui.fragment.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.ContainerActivity
import com.revolgenx.anilib.activity.ToolbarContainerActivity
import com.revolgenx.anilib.common.preference.DISCOVER_NAV_POS
import com.revolgenx.anilib.common.preference.getStartNavigation
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.preference.setStartNavigation
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.common.ui.fragment.ParcelableFragment
import com.revolgenx.anilib.data.meta.TagFilterMetaType
import com.revolgenx.anilib.data.meta.TagFilterSettingMeta
import com.revolgenx.anilib.databinding.ApplicationSettingFragmentLayoutBinding
import com.revolgenx.anilib.ui.dialog.TagFilterSettingDialogFragment
import com.revolgenx.anilib.ui.view.makeToast

class ApplicationSettingFragment:BaseToolbarFragment<ApplicationSettingFragmentLayoutBinding>() {

    override var setHomeAsUp: Boolean = true
    override val title: Int = R.string.application


    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ApplicationSettingFragmentLayoutBinding {
        return ApplicationSettingFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.themeItemView.setOnClickListener {
            ContainerActivity.openActivity(
                requireContext(),
                ParcelableFragment(
                    ThemeControllerFragment::class.java,
                    null
                )
            )
        }

        binding.addRemoveTagIv.setOnClickListener {
            TagFilterSettingDialogFragment.newInstance(
                TagFilterSettingMeta(
                    TagFilterMetaType.TAG
                )
            ).show(
                childFragmentManager,
                TagFilterSettingDialogFragment::class.java.simpleName
            )
        }

        binding.addRemoveGenreIv.setOnClickListener {
            TagFilterSettingDialogFragment.newInstance(
                TagFilterSettingMeta(
                    TagFilterMetaType.GENRE
                )
            ).show(
                childFragmentManager,
                TagFilterSettingDialogFragment::class.java.simpleName
            )
        }
    }


    override fun setSharedPreferenceChangeListener(): Boolean = true

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        super.onSharedPreferenceChanged(sharedPreferences, key)
        when (key) {
            getString(R.string.start_navigation_key) -> {
                if (!requireContext().loggedIn()) {
                    val startNavigation = getStartNavigation(requireContext())
                    if (startNavigation != DISCOVER_NAV_POS) {
                        setStartNavigation(requireContext(), DISCOVER_NAV_POS)
                        makeToast(R.string.please_log_in)
                    }
                }
            }
        }
    }


}