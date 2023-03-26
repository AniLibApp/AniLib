package com.revolgenx.anilib.common.ui.screen

import com.revolgenx.anilib.season.ui.screen.SeasonViewModel
import com.revolgenx.anilib.media.ui.screen.MediaFilterBottomSheetViewModel
import com.revolgenx.anilib.season.data.store.StoreFileName
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { SeasonViewModel(get(), get(named(StoreFileName.seasonFilterFileName))) }
    viewModelOf(::MediaFilterBottomSheetViewModel)
}