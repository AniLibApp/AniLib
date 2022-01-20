package com.revolgenx.anilib.common.viewmodel

import com.revolgenx.anilib.activity.viewmodel.MainActivityViewModel
import com.revolgenx.anilib.list.viewmodel.MediaListCollectionVM
import com.revolgenx.anilib.home.list.viewmodel.AlMediaListCollectionSharedVM
import com.revolgenx.anilib.media.viewmodel.*
import com.revolgenx.anilib.airing.viewmodel.AiringViewModel
import com.revolgenx.anilib.character.viewmodel.CharacterActorViewModel
import com.revolgenx.anilib.character.viewmodel.CharacterContainerViewModel
import com.revolgenx.anilib.character.viewmodel.CharacterMediaViewModel
import com.revolgenx.anilib.character.viewmodel.CharacterViewModel
import com.revolgenx.anilib.entry.viewmodel.EntryEditorViewModel
import com.revolgenx.anilib.friend.viewmodel.FriendViewModel
import com.revolgenx.anilib.home.recommendation.viewmodel.RecommendationViewModel
import com.revolgenx.anilib.home.season.viewmodel.SeasonViewModel
import com.revolgenx.anilib.ui.viewmodel.list.*
import com.revolgenx.anilib.app.setting.viewmodel.NotificationSettingViewModel
import com.revolgenx.anilib.notification.viewmodel.NotificationStoreViewModel
import com.revolgenx.anilib.notification.viewmodel.NotificationViewModel
import com.revolgenx.anilib.review.viewmodel.AllReviewViewModel
import com.revolgenx.anilib.review.viewmodel.ReviewComposerViewModel
import com.revolgenx.anilib.review.viewmodel.ReviewViewModel
import com.revolgenx.anilib.search.viewmodel.SearchFragmentViewModel
import com.revolgenx.anilib.app.setting.data.model.SettingViewModel
import com.revolgenx.anilib.app.setting.data.model.EditTagFilterViewModel
import com.revolgenx.anilib.home.discover.viewmodel.*
import com.revolgenx.anilib.staff.viewmodel.StaffContainerViewModel
import com.revolgenx.anilib.staff.viewmodel.StaffMediaCharacterViewModel
import com.revolgenx.anilib.staff.viewmodel.StaffMediaRoleViewModel
import com.revolgenx.anilib.staff.viewmodel.StaffViewModel
import com.revolgenx.anilib.studio.viewmodel.StudioViewModel
import com.revolgenx.anilib.user.viewmodel.UserFavouriteViewModel
import com.revolgenx.anilib.user.viewmodel.UserFollowerViewModel
import com.revolgenx.anilib.user.viewmodel.UserProfileViewModel
import com.revolgenx.anilib.user.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { SeasonViewModel(get()) }
    viewModel { EntryEditorViewModel(get(), get(), get(), get()) }
    viewModel { MainActivityViewModel(get(), get()) }
    viewModel { MediaInfoViewModel(get(), get(), get(), get()) }

    //overview
    viewModel { MediaOverviewVM(get(), get()) }
    viewModel { MediaWatchViewModel(get()) }
    viewModel { MediaCharacterVM(get()) }
    viewModel { MediaStaffViewModel(get()) }
    viewModel { MediaReviewViewModel(get()) }
    viewModel { MediaStatsViewModel(get()) }
    viewModel { MediaTagDescriptionViewModel() }
    viewModel { MediaSocialFollowingViewModel(get()) }

    //character|staff|studio
    viewModel { CharacterContainerViewModel() }
    viewModel { CharacterViewModel(get(), get()) }
    viewModel { CharacterMediaViewModel(get()) }
    viewModel { CharacterActorViewModel(get()) }
    viewModel { StaffContainerViewModel() }
    viewModel { StaffViewModel(get(), get()) }
    viewModel { StaffMediaCharacterViewModel(get()) }
    viewModel { StaffMediaRoleViewModel(get()) }
    viewModel { StudioViewModel(get(), get()) }

    //search | browse
    viewModel { SearchFragmentViewModel(get()) }

    //recommendation
    viewModel { RecommendationViewModel(get()) }

    //medialist
    viewModel { UserMediaListContainerViewModel() }
    viewModel { WatchingViewModel(get(), get()) }
    viewModel { PlanningViewModel(get(), get()) }
    viewModel { CompletedViewModel(get(), get()) }
    viewModel { DroppedViewModel(get(), get()) }
    viewModel { PausedViewModel(get(), get()) }
    viewModel { RepeatingViewModel(get(), get()) }


    viewModel { MediaListCollectionVM(get(), get()) }
    viewModel { AlMediaListCollectionSharedVM() }


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
    viewModel { NotificationSettingViewModel(get()) }
    viewModel { NotificationStoreViewModel() }

    //airing
    viewModel { AiringViewModel(get()) }

    //discover
    viewModel { DiscoverAiringViewModel(get()) }
    viewModel {
        DiscoverTrendingViewModel(
            get()
        )
    }
    viewModel {
        DiscoverPopularViewModel(
            get()
        )
    }
    viewModel {
        DiscoverNewViewModel(
            get()
        )
    }
    viewModel {
        DiscoverReviewViewModel(
            get()
        )
    }
    viewModel {
        DiscoverWatchingViewModel(
            get(),
            get()
        )
    }
    viewModel {
        DiscoverReadingViewModel(
            get(),
            get()
        )
    }
    viewModel { ShowCaseViewModel(get()) }

    //review
    viewModel { ReviewComposerViewModel(get()) }
    viewModel { ReviewViewModel(get()) }
    viewModel { AllReviewViewModel(get()) }

    //dialog
    viewModel { MediaListingViewModel(get()) }

    //setting
    viewModel { SettingViewModel(get()) }
    viewModel { EditTagFilterViewModel() }

    //friend
    viewModel { FriendViewModel(get()) }
}