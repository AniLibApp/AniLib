package com.revolgenx.anilib.infrastructure.source.home.airing

import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.extensions.HeaderSource
import com.revolgenx.anilib.airing.data.model.AiringScheduleModel

class AiringHeaderSource : HeaderSource<AiringScheduleModel, String>() {

    private var lastHeader: String = ""
    override fun areItemsTheSame(
        first: Data<AiringScheduleModel, String>,
        second: Data<AiringScheduleModel, String>
    ): Boolean {
        return first.anchor.mediaId == second.anchor.mediaId
    }

    override fun dependsOn(source: Source<*>): Boolean {
        return source is AiringSource
    }

    override fun computeHeaders(
        page: Page,
        list: List<AiringScheduleModel>
    ): List<Data<AiringScheduleModel, String>> {
        val results = arrayListOf<Data<AiringScheduleModel, String>>()

        for (airingMediaModel in list) {
            val header =
                airingMediaModel.airingAtModel!!.airingDay

            if (header != lastHeader) {
                results.add(Data(airingMediaModel, header))
                lastHeader = header
            }
        }
        return results

    }
}