package com.revolgenx.anilib.social.ui.fragments.activity_composer.text

import com.revolgenx.anilib.R
import com.revolgenx.anilib.social.data.model.TextActivityModel
import com.revolgenx.anilib.social.ui.fragments.activity_composer.ActivityComposerFragment
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityTextComposerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ActivityTextComposerFragment : ActivityComposerFragment() {
    private val viewModel by sharedViewModel<ActivityTextComposerViewModel>()
    private val activityUnionModel get() = viewModel.activeModel as? TextActivityModel
    override val hintRes: Int = R.string.write_a_status
    override var activityText: String = ""
        set(value) {
            field = value
            viewModel.text = value
        }

    override val defaultText: String? get() = activityUnionModel?.text

}