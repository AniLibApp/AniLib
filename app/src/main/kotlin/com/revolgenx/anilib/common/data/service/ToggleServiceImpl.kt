package com.revolgenx.anilib.common.data.service

import com.revolgenx.anilib.common.data.field.ToggleFavoriteField
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.social.data.field.ToggleActivitySubscriptionField
import com.revolgenx.anilib.social.data.field.ToggleLikeV2Field
import com.revolgenx.anilib.social.ui.model.LikeableUnionModel
import com.revolgenx.anilib.user.data.field.UserToggleFollowField
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class ToggleServiceImpl(
    apolloRepository: ApolloRepository,
    appPreferencesDataStore: AppPreferencesDataStore
) : ToggleService, BaseService(apolloRepository, appPreferencesDataStore) {

    override fun toggleLikeV2(field: ToggleLikeV2Field): Flow<LikeableUnionModel?> {
        return field.toMutation().mapData {
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

    /**
     * True if Toggled successfully, false if any error
     * */
    override fun toggleFavourite(field: ToggleFavoriteField): Flow<Boolean> {
        return field.toMutation().mapData { true }.catch { emit(false) }
    }

    /**
     * True if Un/Subscribed successfully, false if any error
     * */
    override fun toggleActivitySubscription(field: ToggleActivitySubscriptionField): Flow<Boolean> {
        return field.toMutation().mapData {
            it.dataAssertNoErrors.toggleActivitySubscription?.let {
                true
            } ?: false
        }.catch { emit(false) }
    }

    /**
    * Returns true if following/unfollowing successfully, false if any error
    * */
    override fun toggleUserFollow(field: UserToggleFollowField): Flow<Boolean> {
        return field.toMutation().mapData { true }.catch { emit(false) }
    }

}