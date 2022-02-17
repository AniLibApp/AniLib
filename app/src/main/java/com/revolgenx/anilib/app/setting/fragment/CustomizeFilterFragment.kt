package com.revolgenx.anilib.app.setting.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.app.setting.data.meta.TagFilterMetaType
import com.revolgenx.anilib.app.setting.data.meta.TagFilterSettingMeta
import com.revolgenx.anilib.databinding.CustomizeFilterFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenSettingEvent
import com.revolgenx.anilib.infrastructure.event.SettingEventTypes
import com.revolgenx.anilib.infrastructure.event.TagSettingEventMeta

class CustomizeFilterFragment : BaseToolbarFragment<CustomizeFilterFragmentLayoutBinding>() {
    override var titleRes: Int? = R.string.custom_filters
    override var setHomeAsUp: Boolean = true
    override val toolbarColorType: Int = Theme.ColorType.BACKGROUND

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): CustomizeFilterFragmentLayoutBinding {
        return CustomizeFilterFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.addRemoveTagIv.setOnClickListener {
            OpenSettingEvent(
                SettingEventTypes.ADD_REMOVE_TAG_FILTER, TagSettingEventMeta(
                    TagFilterSettingMeta(
                        TagFilterMetaType.TAG
                    )
                )
            ).postEvent
        }

        binding.addRemoveGenreIv.setOnClickListener {
            OpenSettingEvent(
                SettingEventTypes.ADD_REMOVE_TAG_FILTER, TagSettingEventMeta(
                    TagFilterSettingMeta(
                        TagFilterMetaType.GENRE
                    )
                )
            ).postEvent
        }
        binding.addRemoveStreamingOnIv.setOnClickListener {
            OpenSettingEvent(
                SettingEventTypes.ADD_REMOVE_TAG_FILTER, TagSettingEventMeta(
                    TagFilterSettingMeta(
                        TagFilterMetaType.STREAMING_ON
                    )
                )
            ).postEvent

        }
    }
}