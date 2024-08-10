package com.revolgenx.anilib.home.explore.ui.viewmodel

import com.revolgenx.anilib.airing.data.service.AiringScheduleService
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleFilterViewModel
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleViewModel
import com.revolgenx.anilib.common.data.store.AiringScheduleFilterDataStore
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore

class ExploreAiringScheduleViewModel(
    service: AiringScheduleService,
    airingScheduleFilterDataStore: AiringScheduleFilterDataStore,
    appPreferencesDataStore: AppPreferencesDataStore
) : AiringScheduleViewModel(service, airingScheduleFilterDataStore, appPreferencesDataStore)

class ExploreAiringScheduleFilterViewModel(
    airingScheduleFilterDataStore: AiringScheduleFilterDataStore
) : AiringScheduleFilterViewModel(airingScheduleFilterDataStore)