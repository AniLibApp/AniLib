package com.revolgenx.anilib.infrastructure.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.source.BaseRecyclerSource
import com.revolgenx.anilib.home.recommendation.data.model.RecommendationModel
import com.revolgenx.anilib.media.data.field.MediaRecommendationField
import com.revolgenx.anilib.home.recommendation.service.RecommendationService
import io.reactivex.disposables.CompositeDisposable

class MediaOverviewRecommendationSource(
    private val recommendationService: RecommendationService,
    private val compositeDisposable: CompositeDisposable,
    field: MediaRecommendationField
) : BaseRecyclerSource<RecommendationModel, MediaRecommendationField>(field) {

    override fun areItemsTheSame(
        first: RecommendationModel,
        second: RecommendationModel
    ): Boolean = (first.id == second.id) && (first.userRating == second.userRating)

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        recommendationService.getMediaRecommendations(field, compositeDisposable){
            postResult(page, it)
        }
    }
}