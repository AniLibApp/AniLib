package com.revolgenx.anilib.infrastructure.source.stats

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.user.data.field.UserStatsField
import com.revolgenx.anilib.user.data.model.stats.BaseStatisticModel
import com.revolgenx.anilib.user.service.UserStatsService
import com.revolgenx.anilib.common.source.BaseRecyclerSource
import io.reactivex.disposables.CompositeDisposable

class UserStatsSource(
    private val statsService: UserStatsService,
    field: UserStatsField,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<BaseStatisticModel, UserStatsField>(field) {
    override fun areItemsTheSame(
        first: BaseStatisticModel,
        second: BaseStatisticModel
    ): Boolean {
        return first.id == second.id
    }

    override fun getElementType(data: BaseStatisticModel): Int {
        return when (field.userStatsType) {
            UserStatsField.UserStatsType.STAFF, UserStatsField.UserStatsType.VOICE_ACTOR -> {
                1
            }
            else -> {
                0
            }
        }
    }


    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        if (page.isFirstPage()) {
            statsService.getUserStats(field, compositeDisposable) {
                postResult(page, it)
            }
        } else {
            postResult(page, emptyList<BaseStatisticModel>())
        }
    }
}
