package com.revolgenx.anilib.common.viewmodel

import com.revolgenx.anilib.activity.viewmodel.MainActivityViewModel
import com.revolgenx.anilib.list.viewmodel.MediaListCollectionVM
import com.revolgenx.anilib.list.viewmodel.MediaListContainerSharedVM
import com.revolgenx.anilib.media.viewmodel.*
import com.revolgenx.anilib.airing.viewmodel.AiringViewModel
import com.revolgenx.anilib.character.viewmodel.CharacterActorViewModel
import com.revolgenx.anilib.character.viewmodel.CharacterContainerViewModel
import com.revolgenx.anilib.character.viewmodel.CharacterMediaViewModel
import com.revolgenx.anilib.character.viewmodel.CharacterViewModel
import com.revolgenx.anilib.friend.viewmodel.FriendViewModel
import com.revolgenx.anilib.home.recommendation.viewmodel.RecommendationViewModel
import com.revolgenx.anilib.home.season.viewmodel.SeasonViewModel
import com.revolgenx.anilib.app.setting.viewmodel.NotificationSettingViewModel
import com.revolgenx.anilib.notification.viewmodel.NotificationStoreViewModel
import com.revolgenx.anilib.notification.viewmodel.NotificationViewModel
import com.revolgenx.anilib.review.viewmodel.AllReviewViewModel
import com.revolgenx.anilib.review.viewmodel.ReviewComposerVM
import com.revolgenx.anilib.review.viewmodel.ReviewVM
import com.revolgenx.anilib.search.viewmodel.SearchFragmentViewModel
import com.revolgenx.anilib.app.setting.data.model.SettingViewModel
import com.revolgenx.anilib.app.setting.data.model.EditTagFilterViewModel
import com.revolgenx.anilib.entry.viewmodel.MediaListEntryVM
import com.revolgenx.anilib.home.discover.viewmodel.*
import com.revolgenx.anilib.list.viewmodel.AnimeListCollectionStoreVM
import com.revolgenx.anilib.list.viewmodel.MangaListCollectionStoreVM
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
    viewModel { MainActivityViewModel(get(), get()) }
    viewModel { MediaInfoViewModel(get(), get(), get()) }

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

    //list
    viewModel { AnimeListCollectionStoreVM() }
    viewModel { MangaListCollectionStoreVM() }
    viewModel { parameters -> MediaListCollectionVM(get(), get(), parameters.get()) }
    viewModel { MediaListContainerSharedVM() }
    viewModel { MediaListEntryVM(get(), get()) }

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
    viewModel { DiscoverWatchingViewModel(get()) }
    viewModel { DiscoverReadingViewModel(get()) }
    viewModel { ShowCaseViewModel() }

    //review
    viewModel { ReviewComposerVM(get()) }
    viewModel { ReviewVM(get()) }
    viewModel { AllReviewViewModel(get()) }

    //dialog
    viewModel { MediaListingViewModel(get()) }

    //setting
    viewModel { SettingViewModel(get()) }
    viewModel { EditTagFilterViewModel() }

    //friend
    viewModel { FriendViewModel(get()) }
}