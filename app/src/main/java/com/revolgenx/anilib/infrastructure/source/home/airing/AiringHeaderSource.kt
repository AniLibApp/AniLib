package com.revolgenx.anilib.infrastructure.source.home.airing

import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.extensions.HeaderSource
import com.revolgenx.anilib.airing.data.model.AiringMediaModel

class AiringHeaderSource : HeaderSource<AiringMediaModel, String>() {

    private var lastHeader: String = ""
    override fun areItemsTheSame(
        first: Data<AiringMediaModel, String>,
        second: Data<AiringMediaModel, String>
    ): Boolean {
        return first.anchor.mediaId == second.anchor.mediaId
    }

    override fun dependsOn(source: Source<*>): Boolean {
        return source is AiringSource
    }

    override fun computeHeaders(
        page: Page,
        list: List<AiringMediaModel>
    ): List<Data<AiringMediaModel, String>> {
        val results = arrayListOf<Data<AiringMediaModel, String>>()

        for (airingMediaModel in list) {
            val header =
                airingMediaModel.airingTimeModel!!.airingAt!!.airingDay

            if (header != lastHeader) {
                results.add(Data(airingMediaModel, header))
                lastHeader = header
            }
        }
        return results

    }
}