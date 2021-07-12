package com.revolgenx.anilib.social.ui.fragments.activity_composer

import android.view.LayoutInflater
import android.view.ViewGroup
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.ActivityComposerPreviewFragmentLayoutBinding
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.social.markwon.AlStringUtil.anilify

abstract class ActivityComposerPreviewFragment :
    BaseLayoutFragment<ActivityComposerPreviewFragmentLayoutBinding>() {


    protected abstract val activeText: String

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ActivityComposerPreviewFragmentLayoutBinding {
        return ActivityComposerPreviewFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onResume() {
        super.onResume()
        if(isAdded){
            AlMarkwonFactory.getMarkwon()
                .setMarkdown(binding.composerPreviewTv, anilify(activeText))
        }
    }
}