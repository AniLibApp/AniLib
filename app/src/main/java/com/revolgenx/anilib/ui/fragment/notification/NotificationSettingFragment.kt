package com.revolgenx.anilib.ui.fragment.notification

import android.content.SharedPreferences
import android.os.Bundle
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment

class NotificationSettingFragment : BaseLayoutFragment(){
    override val layoutRes: Int = R.layout.notification_setting_fragment_layout
    override var titleRes: Int? = R.string.notification_setting
    override var setHomeAsUp: Boolean = true

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        super.onSharedPreferenceChanged(sharedPreferences, key)
    }
}