package com.revolgenx.anilib.common.ui.viewmodel

import com.revolgenx.anilib.activity.MainActivityViewModel
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleFilterViewModel
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleViewModel
import com.revolgenx.anilib.browse.ui.viewmodel.BrowseViewModel
import com.revolgenx.anilib.character.ui.viewmodel.CharacterAboutViewModel
import com.revolgenx.anilib.character.ui.viewmodel.CharacterActorViewModel
import com.revolgenx.anilib.character.ui.viewmodel.CharacterMediaViewModel
import com.revolgenx.anilib.common.data.store.animeListFilterStoreFileName
import com.revolgenx.anilib.common.data.store.mangaListFilterStoreFileName
import com.revolgenx.anilib.common.data.store.seasonFilterStoreFileName
import com.revolgenx.anilib.entry.ui.viewmodel.MediaListEntryEditorViewModel
import com.revolgenx.anilib.home.recommendation.ui.viewmodel.RecommendationViewModel
import com.revolgenx.anilib.home.season.ui.screen.SeasonViewModel
import com.revolgenx.anilib.list.ui.viewmodel.AnimeListViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MangaListViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MediaListFilterViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaCharacterViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaFilterBottomSheetViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaReviewViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaStaffViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaStatsViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaViewModel
import com.revolgenx.anilib.notification.ui.viewmodel.NotificationViewModel
import com.revolgenx.anilib.relation.ui.viewmodel.UserRelationType
import com.revolgenx.anilib.relation.ui.viewmodel.UserRelationViewModel
import com.revolgenx.anilib.review.ui.viewmodel.ReviewListViewModel
import com.revolgenx.anilib.review.ui.viewmodel.ReviewViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffAboutViewModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffMediaCharacterViewModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffMediaRoleViewModel
import com.revolgenx.anilib.studio.ui.viewmodel.StudioViewModel
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.user.data.field.UserFavouriteType
import com.revolgenx.anilib.user.ui.viewmodel.UserFavouriteViewModel
import com.revolgenx.anilib.user.ui.viewmodel.UserFavouriteContentViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsTypeMediaType
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsOverviewViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsViewModel
import com.revolgenx.anilib.user.ui.viewmodel.UserViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsGenreViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsStaffViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsStudioViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsTagsViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsVoiceActorsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModules = module {
    //main activity
    viewModel { MainActivityViewModel(get()) }

    //airing
    viewModel { AiringScheduleViewModel(get()) }
    viewModel { AiringScheduleFilterViewModel() }

    // media
    viewModel { MediaViewModel(get(), get()) }
    viewModel { MediaCharacterViewModel(get()) }
    viewModel { MediaStaffViewModel(get()) }
    viewModel { MediaReviewViewModel(get()) }
    viewModel { MediaStatsViewModel(get()) }

    // season
    viewModel { SeasonViewModel(get(), get(named(seasonFilterStoreFileName))) }
    viewModel { MediaFilterBottomSheetViewModel(get(named(seasonFilterStoreFileName))) }

    //character
    viewModel { CharacterAboutViewModel(get()) }
    viewModel { CharacterMediaViewModel(get()) }
    viewModel { CharacterActorViewModel(get()) }

    //staff
    viewModel { StaffAboutViewModel(get()) }
    viewModel { StaffMediaCharacterViewModel(get()) }
    viewModel { StaffMediaRoleViewModel(get()) }

    //studio
    viewModel { StudioViewModel(get()) }

    //user
    viewModel { UserViewModel(get(), get()) }
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
    viewModel(named(UserRelationType.USER_RELATION_FOLLOWING)) { UserRelationViewModel(false, get()) }
    viewModel(named(UserRelationType.USER_RELATION_FOLLOWER)) { UserRelationViewModel(true, get()) }


    // notification
    viewModel { NotificationViewModel(get(), get()) }

    //editor
    viewModel { MediaListEntryEditorViewModel(get()) }

    //list
    viewModel { AnimeListViewModel(get(), get(), get(named(animeListFilterStoreFileName))) }
    viewModel { MangaListViewModel(get(), get(), get(named(mangaListFilterStoreFileName))) }
    viewModel { MediaListFilterViewModel() }

    //activity union
    viewModel { ActivityUnionViewModel(get()) }
    viewModel { ActivityComposerViewModel() }

    //browse
    viewModel { BrowseViewModel(get()) }

    //reviews
    viewModel { ReviewListViewModel(get()) }
    viewModel { ReviewViewModel(get()) }

    //recommendations
    viewModel { RecommendationViewModel(get()) }

}