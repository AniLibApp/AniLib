package com.revolgenx.anilib.ui.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.databinding.ApplicationSettingFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenSettingEvent
import com.revolgenx.anilib.infrastructure.event.SettingEventTypes
import com.revolgenx.anilib.ui.dialog.HomeOrderDialog
import com.revolgenx.anilib.ui.dialog.HomePageOrderDialog

class ApplicationSettingFragment : BaseToolbarFragment<ApplicationSettingFragmentLayoutBinding>() {

    override var setHomeAsUp: Boolean = true
    override val titleRes: Int = R.string.application
    override val toolbarColorType: Int = Theme.ColorType.BACKGROUND

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ApplicationSettingFragmentLayoutBinding {
        return ApplicationSettingFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.themeItemView.setOnClickListener {
            OpenSettingEvent(SettingEventTypes.THEME).postEvent
        }

        if (requireContext().loggedIn()) {
            binding.mediaTitlePreference.isEnabled = false
        }


        binding.startUpPageOrder.setOnClickListener {
            HomePageOrderDialog().show(childFragmentManager, HomePageOrderDialog::class.java.simpleName)
        }

        binding.homeScreenOrder.setOnClickListener {
            HomeOrderDialog().show(childFragmentManager, HomeOrderDialog::class.java.simpleName)
        }
    }

}