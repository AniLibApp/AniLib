package com.revolgenx.anilib.social.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.setting.data.store.MediaSettingsPreferencesDataStore
import com.revolgenx.anilib.social.data.field.ActivityReplyField
import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.social.ui.model.ActivityModel
import com.revolgenx.anilib.social.ui.model.ActivityReplyModel
import com.revolgenx.anilib.social.ui.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ActivityUnionServiceImpl(
    apolloRepository: ApolloRepository,
    mediaSettingsPreferencesDataStore: MediaSettingsPreferencesDataStore
) : BaseService(apolloRepository, mediaSettingsPreferencesDataStore),
    ActivityUnionService {
    override fun getActivityUnion(field: ActivityUnionField): Flow<PageModel<ActivityModel>> {
        return field.toQuery().map {
            it.dataAssertNoErrors.page.let {
                PageModel(
                    pageInfo = it.pageInfo.pageInfo,
                    data = it.activities?.mapNotNull {
                        if (!field.canShowAdult && it?.onListActivity?.media?.isAdult == true) return@mapNotNull null

                        it?.onTextActivity?.toModel()
                            ?: it?.onListActivity?.toModel()
                            ?: it?.onMessageActivity?.toModel()
                    }
                )
            }
        }
    }

    override fun getActivityReply(field: ActivityReplyField): Flow<PageModel<ActivityReplyModel>> {
        return field.toQuery().map {
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

}