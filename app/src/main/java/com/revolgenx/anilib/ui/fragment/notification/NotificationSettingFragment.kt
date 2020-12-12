package com.revolgenx.anilib.ui.fragment.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.NotificationSettingFragmentLayoutBinding

class NotificationSettingFragment : BaseLayoutFragment<NotificationSettingFragmentLayoutBinding>(){
    override var titleRes: Int? = R.string.notification_setting
    override var setHomeAsUp: Boolean = true

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): NotificationSettingFragmentLayoutBinding {
        return NotificationSettingFragmentLayoutBinding.inflate(inflater, parent, false)
    }

}