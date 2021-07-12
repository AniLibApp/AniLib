package com.revolgenx.anilib.social.infrastructure.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.infrastructure.service.ActivityUnionService
import io.reactivex.disposables.CompositeDisposable

class ActivityUnionSource(
    field: ActivityUnionField,
    private val activityUnionService: ActivityUnionService,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<ActivityUnionModel, ActivityUnionField>(field) {
    override fun areItemsTheSame(first: ActivityUnionModel, second: ActivityUnionModel): Boolean {
        return first.id == second.id
    }

    override fun getElementType(data: ActivityUnionModel): Int {
        return data.type.ordinal
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        activityUnionService.getActivityUnion(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}