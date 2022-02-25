package com.revolgenx.anilib.home.profile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.UserLoginFragmentLayoutBinding
import com.revolgenx.anilib.common.event.AuthenticateEvent
import com.revolgenx.anilib.common.event.OpenSettingEvent
import com.revolgenx.anilib.common.event.SettingEventTypes
import com.revolgenx.anilib.ui.view.makeConfirmationDialog
import com.revolgenx.anilib.util.openLink

class UserLoginFragment : BaseLayoutFragment<UserLoginFragmentLayoutBinding>() {

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): UserLoginFragmentLayoutBinding {
        return UserLoginFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            settingLayout.setOnClickListener {
                OpenSettingEvent(SettingEventTypes.SETTING).postEvent
            }

            signInLayout.setOnClickListener {
                makeConfirmationDialog(
                    requireContext(),
                    titleRes = R.string.important,
                    messageRes = R.string.sign_in_message,
                    positiveRes = R.string.okay,
                    negativeRes = R.string.cancel,
                ) {
                    AuthenticateEvent().postEvent
                }
            }
            registerLayout.setOnClickListener {
                requireContext().openLink(requireContext().getString(R.string.sign_up_url))
            }

            aboutItemView.setOnClickListener {
                OpenSettingEvent(SettingEventTypes.ABOUT).postEvent
            }

        }
    }
}