package com.revolgenx.anilib.viewmodel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { SeasonViewModel(get()) }
    viewModel { MediaEntryEditorViewModel(get(), get(), get()) }
    viewModel { MainActivityViewModel(get(), get()) }
    viewModel { MediaBrowserViewModel(get(), get()) }

    //overview
    viewModel { MediaOverviewViewModel(get(), get()) }
    viewModel { MediaWatchViewModel(get()) }
    viewModel { MediaCharacterViewModel(get()) }
    viewModel { MediaStaffViewModel(get()) }
    viewModel { MediaReviewViewModel(get()) }
    viewModel { MediaStatsViewModel(get()) }

    //character|staff|studio
    viewModel { CharacterViewModel(get(), get()) }
    viewModel { CharacterMediaViewModel(get()) }
    viewModel { CharacterActorViewModel(get()) }
    viewModel { StaffViewModel(get(), get()) }
    viewModel { StaffMediaCharacterViewModel(get()) }
    viewModel { StaffMediaRoleViewModel(get()) }
    viewModel { StudioViewModel(get(), get()) }

    //search | browse
    viewModel { BrowseActivityViewModel() }
    viewModel { BrowseFragmentViewModel(get()) }

    //recommendation
    viewModel { RecommendationViewModel(get()) }

    //medialist
    viewModel { WatchingViewModel(get(), get()) }
    viewModel { PlanningViewModel(get(), get()) }
    viewModel { CompletedViewModel(get(), get()) }
    viewModel { DroppedViewModel(get(), get()) }
    viewModel { PausedViewModel(get(), get()) }
    viewModel { RepeatingViewModel(get(), get()) }

    //userprofile
    viewModel { UserProfileViewModel(get()) }
    viewModel { UserFollowerViewModel(get()) }
    viewModel { UserFavouriteViewModel(get()) }

    //userstats
    viewModel { StatsOverviewViewModel(get()) }
    viewModel { StatsGenreViewModel(get()) }
    viewModel { StatsTagViewModel(get()) }
    viewModel { StatsVoiceActorViewModel(get()) }
    viewModel { StatsStudioViewModel(get()) }
    viewModel { StatsStaffViewModel(get()) }
}