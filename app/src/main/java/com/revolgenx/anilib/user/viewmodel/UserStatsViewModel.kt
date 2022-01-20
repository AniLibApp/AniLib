package com.revolgenx.anilib.user.viewmodel

import com.revolgenx.anilib.user.data.field.UserStatsField
import com.revolgenx.anilib.user.service.UserStatsService
import com.revolgenx.anilib.infrastructure.source.stats.UserStatsSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

open class UserStatsViewModel(private val userStatsService: UserStatsService) :
    SourceViewModel<UserStatsSource, UserStatsField>() {

    override var field: UserStatsField = UserStatsField()

    override fun createSource(): UserStatsSource {
        source = UserStatsSource(userStatsService, field, compositeDisposable)
        return source!!
    }

}
