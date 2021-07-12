package com.revolgenx.anilib.social.ui.fragments.activity_composer.text

import com.revolgenx.anilib.social.ui.fragments.activity_composer.ActivityComposerPreviewFragment
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityTextComposerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ActivityTextComposerPreviewFragment:ActivityComposerPreviewFragment() {
    private val viewModel by sharedViewModel<ActivityTextComposerViewModel>()

    override val activeText: String
        get() = viewModel.text

}