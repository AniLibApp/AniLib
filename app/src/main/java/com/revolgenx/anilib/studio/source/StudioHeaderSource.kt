package com.revolgenx.anilib.studio.source

import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.extensions.HeaderSource
import com.revolgenx.anilib.infrastructure.source.StudioMediaSource
import com.revolgenx.anilib.media.data.model.MediaModel

class StudioHeaderSource() : HeaderSource<MediaModel, String>() {
    private var lastHeader: String = ""
    override fun dependsOn(source: Source<*>): Boolean {
        return source is StudioMediaSource
    }

    override fun areItemsTheSame(first: Data<MediaModel, String>, second: Data<MediaModel, String>): Boolean {
        return first.anchor.id == second.anchor.id
    }

    override fun computeHeaders(page: Page, list: List<MediaModel>): List<Data<MediaModel, String>> {
        val results = arrayListOf<Data<MediaModel, String>>()
        for (model in list) {
            val header = model.startDate?.year?.toString() ?: "TBA"
            if (header != lastHeader) {
                results.add(Data(model, header))
                lastHeader = header
            }
        }
        return results
    }
}