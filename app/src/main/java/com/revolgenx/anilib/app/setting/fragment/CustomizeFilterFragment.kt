package com.revolgenx.anilib.app.setting.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.app.setting.data.meta.TagFilterMetaType
import com.revolgenx.anilib.app.setting.data.meta.TagFilterSettingMeta
import com.revolgenx.anilib.databinding.CustomizeFilterFragmentLayoutBinding
import com.revolgenx.anilib.common.event.OpenSettingEvent
import com.revolgenx.anilib.common.event.SettingEventTypes
import com.revolgenx.anilib.common.event.TagSettingEventMeta
import com.revolgenx.anilib.common.preference.maxChaptersPref
import com.revolgenx.anilib.common.preference.maxDurationsPref
import com.revolgenx.anilib.common.preference.maxEpisodesPref
import com.revolgenx.anilib.common.preference.maxVolumesPref

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        binding.apply {
            searchMaxEpisodeEt.setText(maxEpisodesPref.toString())
            searchMaxDurationEt.setText(maxDurationsPref.toString())
            searchMaxChapterEt.setText(maxChaptersPref.toString())
            searchMaxVolumeEt.setText(maxVolumesPref.toString())


            searchMaxEpisodeEt.doOnTextChanged { text, _, _, _ ->
                text.toString().toIntOrNull()?.let {
                    maxEpisodesPref = it
                }
            }

            searchMaxDurationEt.doOnTextChanged { text, _, _, _ ->
                text.toString().toIntOrNull()?.let {
                    maxDurationsPref = it
                }
            }

            searchMaxChapterEt.doOnTextChanged { text, _, _, _ ->
                text.toString().toIntOrNull()?.let {
                    maxChaptersPref = it
                }
            }


            searchMaxVolumeEt.doOnTextChanged { text, _, _, _ ->
                text.toString().toIntOrNull()?.let {
                    maxVolumesPref = it
                }
            }


        }
    }
}