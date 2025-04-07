package com.revolgenx.anilib.social.data.service

import com.apollographql.apollo3.api.Optional
import com.revolgenx.anilib.DeleteActivityMutation
import com.revolgenx.anilib.DeleteActivityReplyMutation
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.social.data.field.ActivityInfoField
import com.revolgenx.anilib.social.data.field.ActivityReplyField
import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.social.data.field.SaveActivityReplyField
import com.revolgenx.anilib.social.data.field.SaveMessageActivityField
import com.revolgenx.anilib.social.data.field.SaveTextActivityField
import com.revolgenx.anilib.social.ui.model.ActivityModel
import com.revolgenx.anilib.social.ui.model.ActivityReplyModel
import com.revolgenx.anilib.social.ui.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ActivityUnionServiceImpl(
    apolloRepository: ApolloRepository,
    appPreferencesDataStore: AppPreferencesDataStore
) : BaseService(apolloRepository, appPreferencesDataStore),
    ActivityUnionService {
    override fun getActivityUnion(field: ActivityUnionField): Flow<PageModel<ActivityModel>> {
        return field.toQuery().mapData {
            it.dataAssertNoErrors.page.let {
                PageModel(
                    pageInfo = it.pageInfo.pageInfo,
                    data = it.activities?.mapNotNull {
                        if (!field.canShowAdult && it?.onListActivity?.generalListActivity?.media?.isAdult == true) return@mapNotNull null

                        it?.onTextActivity?.generalTextActivity?.toModel()
                            ?: it?.onListActivity?.generalListActivity?.toModel()
                            ?: it?.onMessageActivity?.generalMessageActivity?.toModel()
                    }
                )
            }
        }
    }

    override fun getActivityInfo(field: ActivityInfoField): Flow<ActivityModel?> {
        return field.toQuery().mapData {response->
            response.dataAssertNoErrors.activity?.let {activity->
                activity.onTextActivity?.let {
                    it.generalTextActivity.toModel()
                        .copy(replies = it.replies?.mapNotNull { it?.activityReply?.toModel() })
                }
                    ?: activity.onListActivity?.let {
                        it.generalListActivity.toModel()
                            .copy(replies = it.replies?.mapNotNull { it?.activityReply?.toModel() })
                    }
                    ?: activity.onMessageActivity?.let {
                        it.generalMessageActivity.toModel()
                            .copy(replies = it.replies?.mapNotNull { it?.activityReply?.toModel() })
                    }
            }
        }
    }

    override fun getActivityReply(field: ActivityReplyField): Flow<PageModel<ActivityReplyModel>> {
        return field.toQuery().mapData {
            it.dataAssertNoErrors.page.let {
                PageModel(
                    pageInfo = it.pageInfo.pageInfo,
                    data = it.activityReplies?.mapNotNull {
                        it?.activityReply?.toModel()
                    }
                )
            }
        }
    }

    override fun saveTextActivity(field: SaveTextActivityField): Flow<Int?> {
        return field.toMutation().mapData {
            it.dataAssertNoErrors.saveTextActivity?.id
        }
    }

    override fun saveMessageActivity(field: SaveMessageActivityField): Flow<Int?> {
        return field.toMutation().mapData {
            it.dataAssertNoErrors.saveMessageActivity?.id
        }
    }

    override fun saveActivityReply(field: SaveActivityReplyField): Flow<Int?> {
        return field.toMutation().mapData {
            it.dataAssertNoErrors.saveActivityReply?.id
        }
    }

    override fun deleteActivity(id: Int): Flow<Boolean> {
        return apolloRepository.mutation(DeleteActivityMutation(id = Optional.present(id)))
            .map {
                it.dataAssertNoErrors.deleteActivity?.deleted == true
            }
    }

    override fun deleteActivityReply(id: Int): Flow<Boolean> {
        return apolloRepository.mutation(DeleteActivityReplyMutation(id = Optional.present(id)))
            .map {
                it.dataAssertNoErrors.deleteActivityReply?.deleted == true
            }
    }

}