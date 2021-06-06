package com.revolgenx.anilib.social.ui.fragments.composer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.ActivityComposerPreviewFragmentLayoutBinding
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.social.markwon.AlStringUtil.anilify
import com.revolgenx.anilib.social.ui.viewmodel.composer.ActivityComposerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ActivityComposerPreviewFragment :
    BaseLayoutFragment<ActivityComposerPreviewFragmentLayoutBinding>() {

    private val viewModel by sharedViewModel<ActivityComposerViewModel>()

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ActivityComposerPreviewFragmentLayoutBinding {
        return ActivityComposerPreviewFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onResume() {
        super.onResume()

        if (viewModel.hasTextChanged) {
            viewModel.hasTextChanged = false
            AlMarkwonFactory.getMarkwon().setMarkdown(binding.composerPreviewTv, anilify(viewModel.text))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.hasTextChanged = true
    }
}