package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.fragment.home.discover.DiscoverAiringViewModel
import com.revolgenx.anilib.viewmodel.airing.AiringViewModel
import com.revolgenx.anilib.viewmodel.browse.BrowseActivityViewModel
import com.revolgenx.anilib.viewmodel.character.CharacterActorViewModel
import com.revolgenx.anilib.viewmodel.character.CharacterMediaViewModel
import com.revolgenx.anilib.viewmodel.character.CharacterViewModel
import com.revolgenx.anilib.viewmodel.home.discover.DiscoverNewViewModel
import com.revolgenx.anilib.viewmodel.home.discover.DiscoverReviewViewModel
import com.revolgenx.anilib.viewmodel.entry.MediaEntryEditorViewModel
import com.revolgenx.anilib.viewmodel.home.RecommendationViewModel
import com.revolgenx.anilib.viewmodel.home.SeasonViewModel
import com.revolgenx.anilib.viewmodel.home.discover.DiscoverReadingViewModel
import com.revolgenx.anilib.viewmodel.home.discover.DiscoverWatchingViewModel
import com.revolgenx.anilib.viewmodel.media.*
import com.revolgenx.anilib.viewmodel.media_list.*
import com.revolgenx.anilib.viewmodel.notification.NotificationViewModel
import com.revolgenx.anilib.viewmodel.review.AllReviewViewModel
import com.revolgenx.anilib.viewmodel.review.ReviewComposerViewModel
import com.revolgenx.anilib.viewmodel.review.ReviewViewModel
import com.revolgenx.anilib.viewmodel.search.SearchFragmentViewModel
import com.revolgenx.anilib.viewmodel.setting.TagFilterSettingDialogViewModel
import com.revolgenx.anilib.viewmodel.staff.StaffMediaCharacterViewModel
import com.revolgenx.anilib.viewmodel.staff.StaffMediaRoleViewModel
import com.revolgenx.anilib.viewmodel.staff.StaffViewModel
import com.revolgenx.anilib.viewmodel.stats.*
import com.revolgenx.anilib.viewmodel.user.UserFavouriteViewModel
import com.revolgenx.anilib.viewmodel.user.UserFollowerViewModel
import com.revolgenx.anilib.viewmodel.user.UserProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { SeasonViewModel(get()) }
    viewModel {
        MediaEntryEditorViewModel(
            get(),
            get(),
            get()
        )
    }
    viewModel { MainActivityViewModel(get(), get()) }
    viewModel {
        MediaBrowserViewModel(
            get(),
            get()
        )
    }

    //overview
    viewModel {
        MediaOverviewViewModel(
            get(),
            get()
        )
    }
    viewModel { MediaWatchViewModel(get()) }
    viewModel { MediaCharacterViewModel(get()) }
    viewModel { MediaStaffViewModel(get()) }
    viewModel { MediaReviewViewModel(get()) }
    viewModel { MediaStatsViewModel(get()) }

    //character|staff|studio
    viewModel {
        CharacterViewModel(
            get(),
            get()
        )
    }
    viewModel {
        CharacterMediaViewModel(
            get()
        )
    }
    viewModel {
        CharacterActorViewModel(
            get()
        )
    }
    viewModel {
        StaffViewModel(
            get(),
            get()
        )
    }
    viewModel {
        StaffMediaCharacterViewModel(
            get()
        )
    }
    viewModel { StaffMediaRoleViewModel(get()) }
    viewModel { StudioViewModel(get(), get()) }

    //search | browse
    viewModel { BrowseActivityViewModel() }
    viewModel { SearchFragmentViewModel(get()) }

    //recommendation
    viewModel { RecommendationViewModel(get()) }

    //medialist
    viewModel {
        WatchingViewModel(
            get(),
            get()
        )
    }
    viewModel {
        PlanningViewModel(
            get(),
            get()
        )
    }
    viewModel {
        CompletedViewModel(
            get(),
            get()
        )
    }
    viewModel {
        DroppedViewModel(
            get(),
            get()
        )
    }
    viewModel {
        PausedViewModel(
            get(),
            get()
        )
    }
    viewModel {
        RepeatingViewModel(
            get(),
            get()
        )
    }

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
    viewModel {
        NotificationViewModel(
            get()
        )
    }

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