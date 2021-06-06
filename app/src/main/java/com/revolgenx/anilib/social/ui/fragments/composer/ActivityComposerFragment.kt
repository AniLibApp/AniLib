package com.revolgenx.anilib.social.ui.fragments.composer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.ActivityComposerFragmentLayoutBinding
import com.revolgenx.anilib.social.data.model.TextActivityModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.social.ui.viewmodel.composer.ActivityComposerViewModel
import com.revolgenx.anilib.util.openLink
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ActivityComposerFragment : BaseLayoutFragment<ActivityComposerFragmentLayoutBinding>() {

    private val viewModel by sharedViewModel<ActivityComposerViewModel>()
    private val activityUnionViewModel by sharedViewModel<ActivityUnionViewModel>()
    private val activityUnionModel get() = activityUnionViewModel.activeActivityUnionModel as? TextActivityModel

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ActivityComposerFragmentLayoutBinding {
        return ActivityComposerFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.activityComposerEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.text = text.toString()
        }

        binding.activityGuidelineTv.setOnClickListener {
            requireContext().openLink(getString(R.string.activity_guide_line_link))
        }

        if(activityUnionModel != null){
            binding.activityComposerEditText.setText(activityUnionModel!!.text)
        }

        binding.markdownHelperLayout.setTextEditView(binding.activityComposerEditText)
    }


    override fun onDestroyView() {
        activityUnionViewModel.activeActivityUnionModel = null
        super.onDestroyView()
    }
}