package com.revolgenx.anilib.common.data.service

import com.revolgenx.anilib.common.data.field.ToggleFavoriteField
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.setting.data.store.MediaSettingsPreferencesDataStore
import com.revolgenx.anilib.social.data.field.ToggleLikeV2Field
import com.revolgenx.anilib.social.ui.model.LikeableUnionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class ToggleServiceImpl(
    apolloRepository: ApolloRepository,
    mediaSettingsPreferencesDataStore: MediaSettingsPreferencesDataStore
) : ToggleService, BaseService(apolloRepository, mediaSettingsPreferencesDataStore) {

    override fun toggleLikeV2(field: ToggleLikeV2Field): Flow<LikeableUnionModel?> {
        return field.toMutation().map {
            it.dataAssertNoErrors.toggleLikeV2?.let { toggleLikeV2 ->
                toggleLikeV2.onTextActivity?.let { m ->
                    LikeableUnionModel(
                        id = m.id,
                        isLiked = m.isLiked ?: false,
                        likeCount = m.likeCount
                    )
                } ?: toggleLikeV2.onListActivity?.let { m ->
                    LikeableUnionModel(
                        id = m.id,
                        isLiked = m.isLiked ?: false,
                        likeCount = m.likeCount
                    )
                } ?: toggleLikeV2.onActivityReply?.let { m ->
                    LikeableUnionModel(
                        id = m.id,
                        isLiked = m.isLiked ?: false,
                        likeCount = m.likeCount
                    )
                } ?: toggleLikeV2.onMessageActivity?.let { m ->
                    LikeableUnionModel(
                        id = m.id,
                        isLiked = m.isLiked ?: false,
                        likeCount = m.likeCount
                    )
                }
            }
        }
    }

    override fun toggleFavourite(field: ToggleFavoriteField): Flow<Boolean> {
        return field.toMutation().map { true }.catch { emit(false) }
    }
}