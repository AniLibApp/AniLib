package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.field.stats.UserStatsField
import com.revolgenx.anilib.service.user.UserStatsService
import com.revolgenx.anilib.source.stats.UserStatsSource

open class UserStatsViewModel(private val userStatsService: UserStatsService) :
    SourceViewModel<UserStatsSource, UserStatsField>() {

    val field = UserStatsField()


    override fun createSource(field: UserStatsField): UserStatsSource {
        source = UserStatsSource(userStatsService, field, compositeDisposable)
        return source!!
    }

}
