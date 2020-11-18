package com.revolgenx.anilib.ui.viewmodel.user

import com.revolgenx.anilib.data.field.stats.UserStatsField
import com.revolgenx.anilib.infrastructure.service.user.UserStatsService
import com.revolgenx.anilib.infrastructure.source.stats.UserStatsSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

open class UserStatsViewModel(private val userStatsService: UserStatsService) :
    SourceViewModel<UserStatsSource, UserStatsField>() {

    override var field: UserStatsField = UserStatsField()

    override fun createSource(): UserStatsSource {
        source = UserStatsSource(userStatsService, field, compositeDisposable)
        return source!!
    }

}
