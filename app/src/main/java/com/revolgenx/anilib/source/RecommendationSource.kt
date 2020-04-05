package com.revolgenx.anilib.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.field.recommendation.RecommendationField
import com.revolgenx.anilib.model.recommendation.RecommendationModel
import com.revolgenx.anilib.service.RecommendationService
import io.reactivex.disposables.CompositeDisposable

class RecommendationSource(
    field: RecommendationField,
    private val recommendationService: RecommendationService,
    private val compositeDisposable: CompositeDisposable
) :
    BaseRecyclerSource<RecommendationModel, RecommendationField>(field) {
    override fun areItemsTheSame(first: RecommendationModel, second: RecommendationModel): Boolean {
        return first.baseId == second.baseId
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        recommendationService.recommendation(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}
