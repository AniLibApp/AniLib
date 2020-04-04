package com.revolgenx.anilib.viewmodel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { SeasonViewModel(get()) }
    viewModel { MediaEntryEditorViewModel(get(), get(), get()) }
    viewModel { MainActivityViewModel(get(), get()) }
    viewModel { MediaBrowserViewModel(get(), get()) }

    viewModel { MediaOverviewViewModel(get(), get()) }
    viewModel { MediaWatchViewModel(get()) }
    viewModel { MediaCharacterViewModel(get()) }
    viewModel { MediaStaffViewModel(get()) }
    viewModel { MediaReviewViewModel(get()) }
    viewModel { MediaStatsViewModel(get()) }

    viewModel { CharacterViewModel(get(), get()) }
    viewModel { CharacterMediaViewModel(get()) }
    viewModel { CharacterActorViewModel(get()) }
    viewModel { StaffViewModel(get(),get()) }
    viewModel { StaffMediaCharacterViewModel(get()) }
    viewModel { StaffMediaRoleViewModel(get()) }
    viewModel { StudioViewModel(get(),get()) }

    viewModel { BrowseActivityViewModel() }
    viewModel { BrowseFragmentViewModel(get()) }
}