package com.revolgenx.anilib.ui.viewmodel

import com.revolgenx.anilib.ui.fragment.home.discover.DiscoverAiringViewModel
import com.revolgenx.anilib.ui.viewmodel.airing.AiringViewModel
import com.revolgenx.anilib.ui.viewmodel.browse.BrowseActivityViewModel
import com.revolgenx.anilib.ui.viewmodel.character.CharacterActorViewModel
import com.revolgenx.anilib.ui.viewmodel.character.CharacterMediaViewModel
import com.revolgenx.anilib.ui.viewmodel.character.CharacterViewModel
import com.revolgenx.anilib.ui.viewmodel.home.discover.DiscoverNewViewModel
import com.revolgenx.anilib.ui.viewmodel.home.discover.DiscoverReviewViewModel
import com.revolgenx.anilib.ui.viewmodel.entry.MediaEntryEditorViewModel
import com.revolgenx.anilib.ui.viewmodel.home.RecommendationViewModel
import com.revolgenx.anilib.ui.viewmodel.home.SeasonViewModel
import com.revolgenx.anilib.ui.viewmodel.home.discover.DiscoverReadingViewModel
import com.revolgenx.anilib.ui.viewmodel.home.discover.DiscoverWatchingViewModel
import com.revolgenx.anilib.ui.viewmodel.media.*
import com.revolgenx.anilib.ui.viewmodel.media_list.*
import com.revolgenx.anilib.ui.viewmodel.notification.NotificationViewModel
import com.revolgenx.anilib.ui.viewmodel.review.AllReviewViewModel
import com.revolgenx.anilib.ui.viewmodel.review.ReviewComposerViewModel
import com.revolgenx.anilib.ui.viewmodel.review.ReviewViewModel
import com.revolgenx.anilib.ui.viewmodel.search.SearchFragmentViewModel
import com.revolgenx.anilib.ui.viewmodel.setting.TagFilterSettingDialogViewModel
import com.revolgenx.anilib.ui.viewmodel.staff.StaffMediaCharacterViewModel
import com.revolgenx.anilib.ui.viewmodel.staff.StaffMediaRoleViewModel
import com.revolgenx.anilib.ui.viewmodel.staff.StaffViewModel
import com.revolgenx.anilib.ui.viewmodel.stats.*
import com.revolgenx.anilib.ui.viewmodel.user.UserFavouriteViewModel
import com.revolgenx.anilib.ui.viewmodel.user.UserFollowerViewModel
import com.revolgenx.anilib.ui.viewmodel.user.UserProfileViewModel
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
    viewModel { SearchFragmentViewModel(get()) }

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

    //notification
    viewModel { NotificationViewModel(get()) }

    //airing
    viewModel { AiringViewModel(get()) }

    //discover
    viewModel { DiscoverAiringViewModel(get()) }
    viewModel { TrendingViewModel(get()) }
    viewModel { PopularViewModel(get()) }
    viewModel { DiscoverNewViewModel(get()) }
    viewModel { DiscoverReviewViewModel(get()) }
    viewModel { DiscoverWatchingViewModel(get(), get()) }
    viewModel { DiscoverReadingViewModel(get(), get()) }

    //review
    viewModel { ReviewComposerViewModel(get()) }
    viewModel { ReviewViewModel(get()) }
    viewModel { AllReviewViewModel(get()) }

    //dialog
    viewModel { MediaViewDialogViewModel(get()) }

    //setting
    viewModel { TagFilterSettingDialogViewModel() }
}