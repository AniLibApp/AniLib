package com.revolgenx.anilib.viewmodel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { SeasonViewModel(get()) }
    viewModel { MediaListEditorViewModel(get(),get()) }
    viewModel { MainActivityViewModel(get(),get()) }
}