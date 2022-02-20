package com.revolgenx.anilib.infrastructure.source.home.airing

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.airing.data.field.AiringMediaField
import com.revolgenx.anilib.airing.data.model.AiringScheduleModel
import com.revolgenx.anilib.airing.service.AiringMediaService
import com.revolgenx.anilib.common.source.BaseRecyclerSource
import io.reactivex.disposables.CompositeDisposable

class AiringSource(
    mediaField: AiringMediaField
    , private val airingMediaService: AiringMediaService
    , private val compositeDisposable: CompositeDisposable
) :
    BaseRecyclerSource<AiringScheduleModel, AiringMediaField>(mediaField) {
    override fun areItemsTheSame(first: AiringScheduleModel, second: AiringScheduleModel): Boolean {
        return first.id == second.id
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
            if (it.data is AiringScheduleModel) {
                (it.data as? AiringScheduleModel)?.commonTimer?.handler?.removeCallbacksAndMessages(
                    null
                )
                (it.data as? AiringScheduleModel)?.commonTimer?.handler = null
            }
        }
    }
}
