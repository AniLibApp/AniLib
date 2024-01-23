package com.revolgenx.anilib.home.explore.ui.viewmodel

import com.revolgenx.anilib.airing.data.service.AiringScheduleService
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleFilterViewModel
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleViewModel
import com.revolgenx.anilib.common.data.store.AiringScheduleFilterDataStore
import com.revolgenx.anilib.common.data.store.AuthPreferencesDataStore

class ExploreAiringScheduleViewModel(
    service: AiringScheduleService,
    airingScheduleFilterDataStore: AiringScheduleFilterDataStore,
    authPreferencesDataStore: AuthPreferencesDataStore
) : AiringScheduleViewModel(service, airingScheduleFilterDataStore, authPreferencesDataStore)

class ExploreAiringScheduleFilterViewModel(
    airingScheduleFilterDataStore: AiringScheduleFilterDataStore
) : AiringScheduleFilterViewModel(airingScheduleFilterDataStore)