package com.revolgenx.anilib.viewmodel.user

import com.revolgenx.anilib.field.stats.UserStatsField
import com.revolgenx.anilib.service.user.UserStatsService
import com.revolgenx.anilib.source.stats.UserStatsSource
import com.revolgenx.anilib.viewmodel.SourceViewModel

open class UserStatsViewModel(private val userStatsService: UserStatsService) :
    SourceViewModel<UserStatsSource, UserStatsField>() {

    override var field: UserStatsField = UserStatsField()

    override fun createSource(): UserStatsSource {
        source = UserStatsSource(userStatsService, field, compositeDisposable)
        return source!!
    }

}
