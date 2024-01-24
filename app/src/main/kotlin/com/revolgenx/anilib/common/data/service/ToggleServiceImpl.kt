package com.revolgenx.anilib.common.data.service

import com.revolgenx.anilib.common.data.field.ToggleFavoriteField
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.setting.data.store.MediaSettingsPreferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class ToggleServiceImpl(
    apolloRepository: ApolloRepository,
    mediaSettingsPreferencesDataStore: MediaSettingsPreferencesDataStore
) : ToggleService, BaseService(apolloRepository, mediaSettingsPreferencesDataStore) {
    override fun toggleFavourite(field: ToggleFavoriteField): Flow<Boolean> {
        return field.toMutation().map { true }.catch { emit(false) }
    }
}