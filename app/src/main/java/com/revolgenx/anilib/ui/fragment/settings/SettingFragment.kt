package com.revolgenx.anilib.ui.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.ToolbarContainerActivity
import com.revolgenx.anilib.appwidget.ui.fragment.AiringWidgetConfigFragment
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.ui.dialog.ReleaseInfoDialog
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.common.ui.fragment.ParcelableFragment
import com.revolgenx.anilib.databinding.SettingFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.event.*
import com.revolgenx.anilib.ui.fragment.notification.NotificationSettingFragment

class SettingFragment : BaseToolbarFragment<SettingFragmentLayoutBinding>() {

    override val titleRes: Int = R.string.settings

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): SettingFragmentLayoutBinding {
        return SettingFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.applicationSettingIv.setOnClickListener {
            SettingEvent(SettingEventTypes.APPLICATION).postEvent
        }

        if (requireContext().loggedIn()) {
            binding.notificationSetting.setOnClickListener {
                SettingEvent(SettingEventTypes.NOTIFICATION).postEvent
            }

            binding.mediaSettingIv.setOnClickListener {
                SettingEvent(SettingEventTypes.MEDIA_SETTING).postEvent
            }

            binding.listSettingIv.setOnClickListener {
                SettingEvent(SettingEventTypes.MEDIA_LIST).postEvent
            }
        } else {
            binding.notificationSetting.visibility = View.GONE
            binding.listSettingIv.visibility = View.GONE
            binding.mediaSettingIv.visibility = View.GONE
        }


        binding.filterSetting.setOnClickListener {
            SettingEvent(SettingEventTypes.CUSTOMIZE_FILTER).postEvent
        }

        binding.widgetSetting.setOnClickListener {
            SettingEvent(SettingEventTypes.AIRING_WIDGET).postEvent
        }

        binding.translationSetting.setOnClickListener {
            SettingEvent(SettingEventTypes.TRANSLATION).postEvent
        }

        if(requireContext().loggedIn()){
            binding.aboutItemView.visibility = View.VISIBLE
            binding.aboutItemView.setOnClickListener {
                SettingEvent(SettingEventTypes.ABOUT).postEvent
            }
        }

    }

}
