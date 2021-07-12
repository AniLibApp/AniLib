package com.revolgenx.anilib.social.ui.fragments.activity_composer.reply

import com.revolgenx.anilib.social.ui.fragments.activity_composer.ActivityComposerPreviewFragment
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityReplyComposerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ActivityReplyComposerPreviewFragment : ActivityComposerPreviewFragment() {
    private val viewModel by sharedViewModel<ActivityReplyComposerViewModel>()
    override val activeText: String
        get() = viewModel.text
}