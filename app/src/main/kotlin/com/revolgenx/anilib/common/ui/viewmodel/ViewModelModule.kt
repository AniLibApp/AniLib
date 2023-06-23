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
import com.revolgenx.anilib.review.ui.viewmodel.ReviewListViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffAboutViewModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffMediaCharacterViewModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffMediaRoleViewModel
import com.revolgenx.anilib.studio.ui.viewmodel.StudioViewModel
import com.revolgenx.anilib.user.ui.viewmodel.UserViewModel
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
    viewModel { MediaViewModel(get(), get() ) }
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

    //recommendations
    viewModel { RecommendationViewModel(get()) }

}