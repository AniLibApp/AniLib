package com.revolgenx.anilib.social.ui.fragments.activity_composer.message

import com.revolgenx.anilib.social.ui.fragments.activity_composer.ActivityComposerPreviewFragment
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityMessageComposerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ActivityMessageComposerPreviewFragment:ActivityComposerPreviewFragment() {
    private val viewModel by sharedViewModel<ActivityMessageComposerViewModel>()

    override val activeText: String
        get() = viewModel.message
}