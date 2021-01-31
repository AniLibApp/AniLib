package com.revolgenx.anilib.ui.fragment.home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.UserLoginFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.event.AuthenticateEvent
import com.revolgenx.anilib.infrastructure.event.SettingEvent
import com.revolgenx.anilib.util.AppUpdater
import com.revolgenx.anilib.util.openLink

class UserLoginFragment:BaseLayoutFragment<UserLoginFragmentLayoutBinding>(){

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): UserLoginFragmentLayoutBinding {
        return UserLoginFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            settingLayout.setOnClickListener {
                SettingEvent().postEvent
            }

            updateCheckLayout.setOnClickListener {
                AppUpdater.startAppUpdater(requireContext(), childFragmentManager, true)
            }

            discordLayout.setOnClickListener {
                requireContext().openLink(getString(R.string.discord_invite_link))
            }

            translateLayout.setOnClickListener {
                requireContext().openLink(getString(R.string.translate_link))
            }

            signInLayout.setOnClickListener {
                AuthenticateEvent().postEvent
            }
        }
    }
}