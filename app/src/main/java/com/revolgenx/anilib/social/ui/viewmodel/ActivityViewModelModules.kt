package com.revolgenx.anilib.social.ui.viewmodel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val activityViewModelModules = module {
    viewModel { ActivityUnionFragmentViewModel(get()) }
}