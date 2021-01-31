package com.revolgenx.anilib.ui.fragment.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.ContainerActivity
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.common.ui.fragment.ParcelableFragment
import com.revolgenx.anilib.databinding.ApplicationSettingFragmentLayoutBinding
import com.revolgenx.anilib.ui.dialog.HomeOrderDialog
import com.revolgenx.anilib.ui.dialog.HomePageOrderDialog
import com.revolgenx.anilib.ui.view.makeToast

class ApplicationSettingFragment : BaseToolbarFragment<ApplicationSettingFragmentLayoutBinding>() {

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