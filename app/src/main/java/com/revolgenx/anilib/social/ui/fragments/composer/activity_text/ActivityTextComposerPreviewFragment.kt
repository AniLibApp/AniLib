package com.revolgenx.anilib.social.ui.fragments.composer.activity_text

import com.revolgenx.anilib.social.ui.fragments.composer.ActivityComposerPreviewFragment
import com.revolgenx.anilib.social.ui.viewmodel.composer.ActivityTextComposerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ActivityTextComposerPreviewFragment:ActivityComposerPreviewFragment() {
    private val viewModel by sharedViewModel<ActivityTextComposerViewModel>()

    override val activeText: String
        get() = viewModel.text

}