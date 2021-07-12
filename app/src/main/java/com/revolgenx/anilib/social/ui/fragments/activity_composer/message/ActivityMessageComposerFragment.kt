package com.revolgenx.anilib.social.ui.fragments.activity_composer.message

import com.revolgenx.anilib.R
import com.revolgenx.anilib.social.data.model.MessageActivityModel
import com.revolgenx.anilib.social.ui.fragments.activity_composer.ActivityComposerFragment
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityMessageComposerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ActivityMessageComposerFragment: ActivityComposerFragment() {
    private val viewModel by sharedViewModel<ActivityMessageComposerViewModel>()
    private val activityUnionModel get() = viewModel.activeModel as? MessageActivityModel
    override val hintRes: Int = R.string.write_a_message
    override var activityText: String = ""
        set(value) {
            field = value
            viewModel.message = value
        }

    override val defaultText: String? get() = activityUnionModel?.message

}