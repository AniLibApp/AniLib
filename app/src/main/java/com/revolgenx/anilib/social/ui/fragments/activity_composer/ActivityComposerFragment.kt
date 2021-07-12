package com.revolgenx.anilib.social.ui.fragments.activity_composer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.ActivityComposerFragmentLayoutBinding
import com.revolgenx.anilib.util.openLink

abstract class ActivityComposerFragment : BaseLayoutFragment<ActivityComposerFragmentLayoutBinding>() {

    abstract var activityText:String
    abstract val defaultText:String?

    abstract val hintRes:Int

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ActivityComposerFragmentLayoutBinding {
        return ActivityComposerFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.activityComposerEditText.setHint(hintRes)
        binding.activityComposerEditText.doOnTextChanged { text, _, _, _ ->
            activityText = text.toString()
        }

        binding.activityGuidelineTv.setOnClickListener {
            requireContext().openLink(getString(R.string.activity_guide_line_link))
        }

        defaultText?.let {
            binding.activityComposerEditText.setText(it)
        }

        binding.markdownHelperLayout.setTextEditView(binding.activityComposerEditText)
    }


}