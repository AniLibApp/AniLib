package com.revolgenx.anilib.source.stats

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.field.stats.UserStatsField
import com.revolgenx.anilib.model.user.stats.BaseStatsModel
import com.revolgenx.anilib.service.user.UserStatsService
import com.revolgenx.anilib.source.BaseRecyclerSource
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
        return first.baseId == second.baseId
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
