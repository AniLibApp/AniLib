package com.revolgenx.anilib.infrastructure.source.home.airing

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.data.field.home.AiringMediaField
import com.revolgenx.anilib.data.model.airing.AiringMediaModel
import com.revolgenx.anilib.infrastructure.service.airing.AiringMediaService
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import io.reactivex.disposables.CompositeDisposable

class AiringSource(
    mediaField: AiringMediaField
    , private val airingMediaService: AiringMediaService
    , private val compositeDisposable: CompositeDisposable
) :
    BaseRecyclerSource<AiringMediaModel, AiringMediaField>(mediaField) {
    override fun areItemsTheSame(first: AiringMediaModel, second: AiringMediaModel): Boolean {
        return first.baseId == second.baseId
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        airingMediaService.getAiringMedia(field, compositeDisposable) {
            postResult(page, it)
        }
    }

    override fun onPageClosed(page: Page) {
        super.onPageClosed(page)
        page.elements().forEach {
            if (it.data is AiringMediaModel) {
                (it.data as? AiringMediaModel)?.airingTimeModel?.commonTimer?.handler?.removeCallbacksAndMessages(
                    null
                )
                (it.data as? AiringMediaModel)?.airingTimeModel?.commonTimer?.handler = null
            }
        }
    }
}
