package com.revolgenx.anilib.social.ui.fragments.composer.activity_reply

import com.revolgenx.anilib.social.ui.fragments.composer.ActivityComposerPreviewFragment
import com.revolgenx.anilib.social.ui.viewmodel.composer.ActivityReplyComposerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ActivityReplyComposerPreviewFragment : ActivityComposerPreviewFragment() {
    private val viewModel by sharedViewModel<ActivityReplyComposerViewModel>()
    override val activeText: String
        get() = viewModel.text
}