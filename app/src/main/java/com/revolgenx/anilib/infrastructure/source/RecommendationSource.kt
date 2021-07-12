package com.revolgenx.anilib.infrastructure.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import com.revolgenx.anilib.data.field.recommendation.RecommendationField
import com.revolgenx.anilib.data.model.recommendation.RecommendationModel
import com.revolgenx.anilib.infrastructure.service.recommendation.RecommendationService
import io.reactivex.disposables.CompositeDisposable

class RecommendationSource(
    field: RecommendationField,
    private val recommendationService: RecommendationService,
    private val compositeDisposable: CompositeDisposable
) :
    BaseRecyclerSource<RecommendationModel, RecommendationField>(field) {
    override fun areItemsTheSame(first: RecommendationModel, second: RecommendationModel): Boolean {
        return first.id == second.id
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        recommendationService.recommendation(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}
