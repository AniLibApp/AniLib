package com.revolgenx.anilib.infrastructure.source.stats

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.data.field.stats.UserStatsField
import com.revolgenx.anilib.data.model.user.stats.BaseStatsModel
import com.revolgenx.anilib.infrastructure.service.user.UserStatsService
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import io.reactivex.disposables.CompositeDisposable

class UserStatsSource(
    private val statsService: UserStatsService,
    field: UserStatsField,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<BaseStatsModel, UserStatsField>(field) {
    override fun areItemsTheSame(
        first: BaseStatsModel,
        second: BaseStatsModel
    ): Boolean {
        return first.id == second.id
    }

    override fun getElementType(data: BaseStatsModel): Int {
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
            postResult(page, emptyList<BaseStatsModel>())
        }
    }
}
