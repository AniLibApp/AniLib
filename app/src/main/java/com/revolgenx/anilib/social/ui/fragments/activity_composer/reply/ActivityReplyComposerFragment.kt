package com.revolgenx.anilib.social.ui.fragments.activity_composer.reply

import com.revolgenx.anilib.R
import com.revolgenx.anilib.social.ui.fragments.activity_composer.ActivityComposerFragment
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityReplyComposerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ActivityReplyComposerFragment : ActivityComposerFragment() {
    private val viewModel by sharedViewModel<ActivityReplyComposerViewModel>()
    override val hintRes: Int = R.string.write_a_reply
    override var activityText: String = ""
        set(value) {
            field = value
            viewModel.text = value
        }

    override val defaultText: String?
        get() = viewModel.activeModel?.text


}