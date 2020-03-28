package com.revolgenx.anilib.source

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.extensions.MainSource
import com.revolgenx.anilib.model.MediaRecommendationModel
import com.revolgenx.anilib.field.overview.MediaRecommendationField
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.service.RecommendationService
import io.reactivex.disposables.CompositeDisposable
import java.lang.Exception

class MediaOverviewRecommendationSource(
    private val recommendationService: RecommendationService,
    private val compositeDisposable: CompositeDisposable,
    field: MediaRecommendationField
) : BaseRecyclerSource<MediaRecommendationModel, MediaRecommendationField>(field) {

    override fun areItemsTheSame(
        first: MediaRecommendationModel,
        second: MediaRecommendationModel
    ): Boolean = (first.recommendationId == second.recommendationId) && (first.userRating == second.userRating)

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        recommendationService.mediaRecommendation(field, compositeDisposable){
            postResult(page, it)
        }
    }
}