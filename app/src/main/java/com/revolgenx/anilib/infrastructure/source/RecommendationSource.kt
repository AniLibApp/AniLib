package com.revolgenx.anilib.infrastructure.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.source.BaseRecyclerSource
import com.revolgenx.anilib.home.recommendation.data.field.RecommendationField
import com.revolgenx.anilib.home.recommendation.data.model.RecommendationModel
import com.revolgenx.anilib.home.recommendation.service.RecommendationService
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
        recommendationService.getRecommendations(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}
