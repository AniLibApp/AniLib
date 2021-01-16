package com.revolgenx.anilib.ui.fragment.settings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.data.meta.TagFilterMetaType
import com.revolgenx.anilib.data.meta.TagFilterSettingMeta
import com.revolgenx.anilib.databinding.CustomizeFilterFragmentLayoutBinding
import com.revolgenx.anilib.ui.dialog.TagFilterSettingDialogFragment

class CustomizeFilterFragment : BaseLayoutFragment<CustomizeFilterFragmentLayoutBinding>() {
    override var titleRes: Int? = R.string.custom_filters
    override var setHomeAsUp: Boolean = true

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): CustomizeFilterFragmentLayoutBinding {
        return CustomizeFilterFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.addRemoveTagIv.setOnClickListener {
            TagFilterSettingDialogFragment.newInstance(
                TagFilterSettingMeta(
                    TagFilterMetaType.TAG
                )
            ).show(
                childFragmentManager,
                TagFilterSettingDialogFragment::class.java.simpleName
            )
        }

        binding.addRemoveGenreIv.setOnClickListener {
            TagFilterSettingDialogFragment.newInstance(
                TagFilterSettingMeta(
                    TagFilterMetaType.GENRE
                )
            ).show(
                childFragmentManager,
                TagFilterSettingDialogFragment::class.java.simpleName
            )
        }
    }
}