package com.revolgenx.anilib.common.ui.screen

import com.revolgenx.anilib.activity.MainActivityViewModel
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleFilterViewModel
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleViewModel
import com.revolgenx.anilib.character.ui.viewmodel.CharacterAboutViewModel
import com.revolgenx.anilib.common.data.store.StoreFileName
import com.revolgenx.anilib.entry.ui.viewmodel.MediaListEntryEditorViewModel
import com.revolgenx.anilib.home.season.ui.screen.SeasonViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaFilterBottomSheetViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaCharacterViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaOverviewViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaReviewViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaStaffViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaViewModel
import com.revolgenx.anilib.notification.ui.viewmodel.NotificationViewModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffAboutViewModel
import com.revolgenx.anilib.studio.ui.viewmodel.StudioViewModel
import com.revolgenx.anilib.user.ui.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModules = module {
    //main activity
    viewModel { MainActivityViewModel() }

    //airing
    viewModel { AiringScheduleViewModel(get()) }
    viewModel { AiringScheduleFilterViewModel() }

    // media
    viewModel { MediaViewModel() }
    viewModel { MediaOverviewViewModel(get()) }
    viewModel { MediaCharacterViewModel(get()) }
    viewModel { MediaStaffViewModel(get()) }
    viewModel { MediaReviewViewModel(get()) }

    // season
    viewModel { SeasonViewModel(get(), get(named(StoreFileName.seasonFieldFileName))) }
    viewModel { MediaFilterBottomSheetViewModel(get(named(StoreFileName.seasonFieldFileName))) }

    //character
    viewModel { CharacterAboutViewModel(get()) }

    //staff
    viewModel { StaffAboutViewModel(get()) }

    //studio
    viewModel {StudioViewModel(get())}

    //user
    viewModel { UserViewModel(get(), get()) }

    // notification
    viewModel { NotificationViewModel(get(), get()) }

    //editor
    viewModel { MediaListEntryEditorViewModel(get()) }



}