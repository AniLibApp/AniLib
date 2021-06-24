package com.revolgenx.anilib.social.ui.viewmodel

import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityMessageComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityReplyComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityTextComposerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val activityViewModelModules = module {
    viewModel { ActivityUnionViewModel(get(), get(), get()) }
    viewModel { ActivityInfoViewModel(get()) }
    viewModel { ActivityTextComposerViewModel(get()) }
    viewModel { ActivityMessageComposerViewModel(get()) }
    viewModel { ActivityReplyComposerViewModel(get()) }
    viewModel { ActivityReplyViewModel(get(), get()) }
}