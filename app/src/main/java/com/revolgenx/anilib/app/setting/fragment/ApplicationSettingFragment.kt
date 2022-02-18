package com.revolgenx.anilib.app.setting.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.databinding.ApplicationSettingFragmentLayoutBinding
import com.revolgenx.anilib.app.setting.dialog.DiscoverOrderDialog
import com.revolgenx.anilib.app.setting.dialog.HomePageOrderDialog
import com.revolgenx.anilib.util.loginContinue

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

        loginContinue(false) {
            binding.mediaTitlePreference.isEnabled = false
        }


        binding.startUpPageOrder.setOnClickListener {
            HomePageOrderDialog().show(childFragmentManager, HomePageOrderDialog::class.java.simpleName)
        }

        binding.homeScreenOrder.setOnClickListener {
            DiscoverOrderDialog().show(childFragmentManager, DiscoverOrderDialog::class.java.simpleName)
        }
    }

}