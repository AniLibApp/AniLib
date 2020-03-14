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

class BrowserOverviewRecommendationSource(
    private val recommendationService: RecommendationService,
    private val field: MediaRecommendationField,
    private val lifeCycleOwner: LifecycleOwner,
    private val compositeDisposable: CompositeDisposable? = null,
    private val pageSize: Int = 10
) : MainSource<MediaRecommendationModel>() {

    override fun areItemsTheSame(
        first: MediaRecommendationModel,
        second: MediaRecommendationModel
    ): Boolean = first.mediaId == second.mediaId && first.userRating == second.userRating

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        var key = 1
        if (page.isFirstPage()) {
            setKey(page, 1)
        } else {
            key = getKey<Int>(page.previous()!!)!!.plus(1)
            setKey(page, key)
        }

        field.also {
            it.page = key
            it.perPage = pageSize
        }

        recommendationService.mediaRecommendation(field, compositeDisposable)
            .observe(lifeCycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> postResult(page, it.data!!)
                    Status.ERROR -> postResult(page, Exception(it.message))
                }
            }
    }
}