package com.revolgenx.anilib.social.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.ext.onIO
import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.social.ui.model.ActivityUnionModel
import com.revolgenx.anilib.social.ui.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ActivityUnionServiceImpl(apolloRepository: ApolloRepository) : BaseService(apolloRepository),
    ActivityUnionService {
    override fun getActivityUnion(field: ActivityUnionField): Flow<PageModel<ActivityUnionModel>> {
        return field.toQuery().map {
            it.dataAssertNoErrors.page.let {
                PageModel(
                    pageInfo = it.pageInfo.pageInfo,
                    data = it.activities?.map {
                        ActivityUnionModel(
                            listActivityModel = it?.onListActivity
                                ?.takeIf { /* if (field.canShowAdult) true else it.media?.isAdult == false*/ true }
                                ?.toModel(),
                            messageActivityModel = it?.onMessageActivity?.toModel(),
                            textActivityModel = it?.onTextActivity?.toModel()
                        )
                    }
                )
            }
        }.onIO()
    }
}