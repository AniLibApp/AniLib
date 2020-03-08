package com.revolgenx.anilib.viewmodel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { SeasonViewModel(get()) }
    viewModel { MediaEntryEditorViewModel(get(),get(),get()) }
    viewModel { MainActivityViewModel(get(),get()) }
    viewModel { MediaBrowserViewModel(get()) }
}