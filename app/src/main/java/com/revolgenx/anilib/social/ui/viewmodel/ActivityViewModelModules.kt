package com.revolgenx.anilib.social.ui.viewmodel

import com.revolgenx.anilib.social.ui.viewmodel.composer.ActivityReplyComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.composer.ActivityTextComposerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val activityViewModelModules = module {
    viewModel { ActivityUnionViewModel(get(), get(), get()) }
    viewModel { ActivityInfoViewModel(get()) }
    viewModel { ActivityTextComposerViewModel(get()) }
    viewModel { ActivityReplyComposerViewModel(get()) }
    viewModel { ActivityReplyViewModel(get(), get()) }
}