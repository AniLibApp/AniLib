package com.revolgenx.anilib.common.ui.viewmodel

import android.content.Context
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleFilterViewModel
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleViewModel
import com.revolgenx.anilib.app.ui.viewmodel.MainActivityViewModel
import com.revolgenx.anilib.app.ui.viewmodel.ScrollViewModel
import com.revolgenx.anilib.app.ui.viewmodel.SharedActivityViewModel
import com.revolgenx.anilib.browse.ui.viewmodel.BrowseFilterViewModel
import com.revolgenx.anilib.browse.ui.viewmodel.BrowseViewModel
import com.revolgenx.anilib.character.ui.viewmodel.CharacterAboutViewModel
import com.revolgenx.anilib.character.ui.viewmodel.CharacterActorViewModel
import com.revolgenx.anilib.character.ui.viewmodel.CharacterMediaFilterViewModel
import com.revolgenx.anilib.character.ui.viewmodel.CharacterMediaViewModel
import com.revolgenx.anilib.common.data.store.activityUnionFilterDataStore
import com.revolgenx.anilib.common.data.store.airingScheduleFilterDataStore
import com.revolgenx.anilib.common.data.store.animeListFilterDataStore
import com.revolgenx.anilib.common.data.store.browseFilterDataStore
import com.revolgenx.anilib.common.data.store.exploreAiringScheduleFilterDataStore
import com.revolgenx.anilib.common.data.store.exploreNewlyAddedDataStore
import com.revolgenx.anilib.common.data.store.explorePopularDataStore
import com.revolgenx.anilib.common.data.store.exploreTrendingDataStore
import com.revolgenx.anilib.common.data.store.genreCollectionDataStore
import com.revolgenx.anilib.common.data.store.mangaListFilterDataStore
import com.revolgenx.anilib.common.data.store.mediaTagCollectionDataStore
import com.revolgenx.anilib.common.data.store.readableOnCollectionDataStore
import com.revolgenx.anilib.common.data.store.seasonFilterDataStore
import com.revolgenx.anilib.common.data.store.streamingOnCollectionDataStore
import com.revolgenx.anilib.common.ui.ads.AdsViewModel
import com.revolgenx.anilib.entry.ui.viewmodel.MediaListEntryEditorViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreAiringScheduleFilterViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreAiringScheduleViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreMediaListFilterViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreMediaViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreNewlyAddedFilterViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExplorePopularFilterViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreReadingViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreTrendingFilterViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreWatchingViewModel
import com.revolgenx.anilib.home.recommendation.ui.viewmodel.RecommendationFilterViewModel
import com.revolgenx.anilib.home.recommendation.ui.viewmodel.RecommendationViewModel
import com.revolgenx.anilib.home.season.ui.screen.SeasonViewModel
import com.revolgenx.anilib.home.season.ui.viewmodel.SeasonFilterViewModel
import com.revolgenx.anilib.list.ui.viewmodel.AnimeListFilterViewModel
import com.revolgenx.anilib.list.ui.viewmodel.AnimeListViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MangaListFilterViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MangaListViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MediaListCompareViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaCharacterFilterViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaCharacterViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaRecommendationViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaReviewViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaSocialFollowingScreenViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaSocialScreenViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaStaffViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaStatsViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaViewModel
import com.revolgenx.anilib.notification.ui.viewmodel.NotificationViewModel
import com.revolgenx.anilib.relation.ui.viewmodel.UserRelationType
import com.revolgenx.anilib.relation.ui.viewmodel.UserRelationViewModel
import com.revolgenx.anilib.review.ui.viewmodel.ReviewComposerViewModel
import com.revolgenx.anilib.review.ui.viewmodel.ReviewListFilterViewModel
import com.revolgenx.anilib.review.ui.viewmodel.ReviewListViewModel
import com.revolgenx.anilib.review.ui.viewmodel.ReviewViewModel
import com.revolgenx.anilib.setting.ui.viewmodel.AppearanceSettingsViewModel
import com.revolgenx.anilib.setting.ui.viewmodel.BillingViewModel
import com.revolgenx.anilib.setting.ui.viewmodel.FilterSettingsViewModel
import com.revolgenx.anilib.setting.ui.viewmodel.GeneralSettingsViewModel
import com.revolgenx.anilib.setting.ui.viewmodel.GenreFilterSettingsViewModel
import com.revolgenx.anilib.setting.ui.viewmodel.MediaListSettingsViewModel
import com.revolgenx.anilib.setting.ui.viewmodel.MediaSettingsViewModel
import com.revolgenx.anilib.setting.ui.viewmodel.NotificationSettingsViewModel
import com.revolgenx.anilib.setting.ui.viewmodel.SettingsViewModel
import com.revolgenx.anilib.setting.ui.viewmodel.TagFilterSettingsViewModel
import com.revolgenx.anilib.setting.ui.viewmodel.WidgetSettingsViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityInfoViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityReplyServiceViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityReplyViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionFilterViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionServiceViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.social.ui.viewmodel.MainActivityUnionViewModel
import com.revolgenx.anilib.social.ui.viewmodel.MessageComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ReplyComposerViewModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffAboutViewModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffMediaCharacterFilterViewModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffMediaCharacterViewModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffMediaRoleFilterViewModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffMediaRoleViewModel
import com.revolgenx.anilib.studio.ui.viewmodel.StudioFilterViewModel
import com.revolgenx.anilib.studio.ui.viewmodel.StudioViewModel
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.user.data.field.UserFavouriteType
import com.revolgenx.anilib.user.ui.viewmodel.UserActivityFilterViewModel
import com.revolgenx.anilib.user.ui.viewmodel.UserFavouriteContentViewModel
import com.revolgenx.anilib.user.ui.viewmodel.UserFavouriteViewModel
import com.revolgenx.anilib.user.ui.viewmodel.UserViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsGenreViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsOverviewViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsStaffViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsStudioViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsTagsViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsTypeMediaType
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsVoiceActorsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModules = module {
    //main activity
    viewModel {
        MainActivityViewModel(
            get(),
            get(),
            get<Context>().airingScheduleFilterDataStore,
            get<Context>().exploreAiringScheduleFilterDataStore
        )
    }
    viewModel { SharedActivityViewModel() }

    //airing
    viewModel {
        AiringScheduleViewModel(
            get(),
            get<Context>().airingScheduleFilterDataStore,
            get()
        )
    }
    viewModel { AiringScheduleFilterViewModel(get<Context>().airingScheduleFilterDataStore) }
    viewModel { ExploreViewModel(get()) }
    viewModel {
        ExploreAiringScheduleViewModel(
            get(),
            get<Context>().exploreAiringScheduleFilterDataStore,
            get()
        )
    }
    viewModel { ExploreAiringScheduleFilterViewModel(get<Context>().exploreAiringScheduleFilterDataStore) }
    viewModel { ExploreMediaListFilterViewModel() }

    // media
    viewModel { MediaViewModel(get(), get(), get(), get()) }
    viewModel { MediaRecommendationViewModel(get()) }
    viewModel { MediaCharacterViewModel(get()) }
    viewModel { MediaCharacterFilterViewModel() }
    viewModel { MediaStaffViewModel(get()) }
    viewModel { MediaReviewViewModel(get()) }
    viewModel { MediaStatsViewModel(get()) }
    viewModel { MediaSocialScreenViewModel() }
    viewModel { MediaSocialFollowingScreenViewModel(get()) }

    // season
    viewModel { SeasonViewModel(get(), get<Context>().seasonFilterDataStore) }
    viewModel {
        SeasonFilterViewModel(
            get<Context>().seasonFilterDataStore,
            get<Context>().mediaTagCollectionDataStore,
            get<Context>().genreCollectionDataStore,
            get()
        )
    }

    //character
    viewModel { CharacterAboutViewModel(get()) }
    viewModel { CharacterMediaViewModel(get()) }
    viewModel { CharacterMediaFilterViewModel(get()) }
    viewModel { CharacterActorViewModel(get()) }

    //staff
    viewModel { StaffAboutViewModel(get()) }
    viewModel { StaffMediaCharacterViewModel(get()) }
    viewModel { StaffMediaCharacterFilterViewModel(get()) }
    viewModel { StaffMediaRoleFilterViewModel(get()) }
    viewModel { StaffMediaRoleViewModel(get()) }

    //studio
    viewModel { StudioViewModel(get()) }
    viewModel { StudioFilterViewModel(get()) }

    //user
    viewModel { UserViewModel(get(), get()) }
    viewModel { UserActivityFilterViewModel() }
    viewModel { UserFavouriteViewModel() }
    viewModel(named(UserFavouriteType.FAVOURITE_ANIME)) {
        UserFavouriteContentViewModel(
            UserFavouriteType.FAVOURITE_ANIME,
            get()
        )
    }
    viewModel(named(UserFavouriteType.FAVOURITE_MANGA)) {
        UserFavouriteContentViewModel(
            UserFavouriteType.FAVOURITE_MANGA,
            get()
        )
    }
    viewModel(named(UserFavouriteType.CHARACTER)) {
        UserFavouriteContentViewModel(
            UserFavouriteType.CHARACTER,
            get()
        )
    }
    viewModel(named(UserFavouriteType.STAFF)) {
        UserFavouriteContentViewModel(
            UserFavouriteType.STAFF,
            get()
        )
    }
    viewModel(named(UserFavouriteType.STUDIO)) {
        UserFavouriteContentViewModel(
            UserFavouriteType.STUDIO,
            get()
        )
    }
    viewModel(named(UserStatsTypeMediaType.USER_STATS_ANIME)) {
        UserStatsViewModel()
    }
    viewModel(named(UserStatsTypeMediaType.USER_STATS_MANGA)) {
        UserStatsViewModel()
    }
    viewModel(named(UserStatsTypeMediaType.USER_STATS_OVERVIEW_ANIME)) {
        UserStatsOverviewViewModel(MediaType.ANIME, get())
    }
    viewModel(named(UserStatsTypeMediaType.USER_STATS_OVERVIEW_MANGA)) {
        UserStatsOverviewViewModel(MediaType.MANGA, get())
    }
    viewModel(named(UserStatsTypeMediaType.USER_STATS_GENRE_ANIME)) {
        UserStatsGenreViewModel(MediaType.ANIME, get())
    }
    viewModel(named(UserStatsTypeMediaType.USER_STATS_GENRE_MANGA)) {
        UserStatsGenreViewModel(MediaType.MANGA, get())
    }
    viewModel(named(UserStatsTypeMediaType.USER_STATS_TAGS_ANIME)) {
        UserStatsTagsViewModel(MediaType.ANIME, get())
    }
    viewModel(named(UserStatsTypeMediaType.USER_STATS_TAGS_MANGA)) {
        UserStatsTagsViewModel(MediaType.MANGA, get())
    }
    viewModel(named(UserStatsTypeMediaType.USER_STATS_STAFF_ANIME)) {
        UserStatsStaffViewModel(MediaType.ANIME, get())
    }
    viewModel(named(UserStatsTypeMediaType.USER_STATS_STAFF_MANGA)) {
        UserStatsStaffViewModel(MediaType.MANGA, get())
    }

    viewModel { UserStatsVoiceActorsViewModel(get()) }
    viewModel { UserStatsStudioViewModel(get()) }

    // user relation
    viewModel(named(UserRelationType.USER_RELATION_FOLLOWING)) {
        UserRelationViewModel(
            false,
            get()
        )
    }
    viewModel(named(UserRelationType.USER_RELATION_FOLLOWER)) { UserRelationViewModel(true, get()) }


    // notification
    viewModel { NotificationViewModel(get(), get()) }

    //editor
    viewModel { MediaListEntryEditorViewModel(get(), get(), get(), get()) }

    //list
    viewModel { AnimeListViewModel(get(), get(), get(), animeListFilterDataStore()) }
    viewModel { MangaListViewModel(get(), get(), get(), mangaListFilterDataStore()) }
    viewModel { AnimeListFilterViewModel(get()) }
    viewModel { MangaListFilterViewModel(get()) }
    viewModel { MediaListCompareViewModel(get()) }

    //activity union
    viewModel {
        MainActivityUnionViewModel(
            get(),
            get<Context>().activityUnionFilterDataStore
        )
    }
    viewModel { ActivityUnionViewModel(get()) }
    viewModel { ActivityUnionServiceViewModel(get(), get()) }
    viewModel { ActivityReplyServiceViewModel(get(), get()) }
    viewModel { ActivityComposerViewModel(get()) }
    viewModel { MessageComposerViewModel(get()) }
    viewModel { ReplyComposerViewModel(get()) }
    viewModel { ActivityUnionFilterViewModel(get<Context>().activityUnionFilterDataStore) }
    viewModel { parameters -> ActivityReplyViewModel(parameters.get(), get()) }
    viewModel { parameters -> ActivityInfoViewModel(parameters.get(), get()) }

    //browse
    viewModel {
        BrowseViewModel(
            get<Context>().mediaTagCollectionDataStore,
            get<Context>().genreCollectionDataStore,
            get(),
            get()
        )
    }
    viewModel {
        BrowseFilterViewModel(
            get<Context>().mediaTagCollectionDataStore,
            get<Context>().genreCollectionDataStore,
            get<Context>().streamingOnCollectionDataStore,
            get<Context>().readableOnCollectionDataStore,
            get<Context>().browseFilterDataStore,
            get()
        )
    }

    //reviews
    viewModel { ReviewListViewModel(get()) }
    viewModel { ReviewListFilterViewModel() }
    viewModel { ReviewViewModel(get()) }
    viewModel { ReviewComposerViewModel(get(), get()) }

    //recommendations
    viewModel { RecommendationViewModel(get()) }
    viewModel { RecommendationFilterViewModel() }

    //settings
    viewModel { SettingsViewModel(get()) }
    viewModel { GeneralSettingsViewModel(get()) }
    viewModel { AppearanceSettingsViewModel(get(), get(), get(), get()) }
    viewModel { MediaSettingsViewModel(get(), get(), get()) }
    viewModel { NotificationSettingsViewModel(get(), get(), get()) }
    viewModel { MediaListSettingsViewModel(get(), get()) }
    viewModel { FilterSettingsViewModel(get(), get()) }
    viewModel { TagFilterSettingsViewModel(get<Context>().mediaTagCollectionDataStore, get()) }
    viewModel { GenreFilterSettingsViewModel(get<Context>().genreCollectionDataStore, get()) }
    viewModel { WidgetSettingsViewModel(get(), get(), get()) }
    //explore
    viewModel {
        ExploreMediaViewModel.ExploreTrendingViewModel(
            get(),
            get<Context>().exploreTrendingDataStore
        )
    }
    viewModel {
        ExploreMediaViewModel.ExplorePopularViewModel(
            get(),
            get<Context>().explorePopularDataStore
        )
    }
    viewModel {
        ExploreMediaViewModel.ExploreNewlyAddedViewModel(
            get(),
            get<Context>().exploreNewlyAddedDataStore
        )
    }
    viewModel {
        ExploreTrendingFilterViewModel(
            get<Context>().exploreTrendingDataStore,
            get<Context>().mediaTagCollectionDataStore,
            get<Context>().genreCollectionDataStore,
            get()
        )
    }
    viewModel {
        ExplorePopularFilterViewModel(
            get<Context>().explorePopularDataStore,
            get<Context>().mediaTagCollectionDataStore,
            get<Context>().genreCollectionDataStore,
            get()
        )
    }
    viewModel {
        ExploreNewlyAddedFilterViewModel(
            get<Context>().exploreNewlyAddedDataStore,
            get<Context>().mediaTagCollectionDataStore,
            get<Context>().genreCollectionDataStore,
            get()
        )
    }
    viewModel { ExploreWatchingViewModel(get(), get(), get()) }
    viewModel { ExploreReadingViewModel(get(), get(), get()) }


    viewModel { AdsViewModel(get(), get()) }
    viewModel { ScrollViewModel() }
    viewModel { BillingViewModel(get()) }
}