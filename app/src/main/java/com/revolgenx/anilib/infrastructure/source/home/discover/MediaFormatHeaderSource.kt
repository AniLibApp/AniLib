package com.revolgenx.anilib.infrastructure.source.home.discover

import android.content.Context
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.extensions.HeaderSource
import com.revolgenx.anilib.R
import com.revolgenx.anilib.infrastructure.source.MediaAdapterSource
import com.revolgenx.anilib.media.data.model.MediaModel

class MediaFormatHeaderSource(context: Context) : HeaderSource<MediaModel, String>() {

    private val mediaFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }
    private var lastHeader: String = ""
    override fun areItemsTheSame(
        first: Data<MediaModel, String>,
        second: Data<MediaModel, String>
    ): Boolean {
        return first.anchor.id == second.anchor.id
    }

    override fun dependsOn(source: Source<*>): Boolean {
        return source is MediaAdapterSource
    }

    override fun computeHeaders(
        page: Page,
        list: List<MediaModel>
    ): List<Data<MediaModel, String>> {
        val results = arrayListOf<Data<MediaModel, String>>()

        for (mediaModel in list) {
            val header =
                mediaModel.format?.let { mediaFormats[it] } ?: ""

            if (header != lastHeader) {
                results.add(Data(mediaModel, header))
                lastHeader = header
            }
        }
        return results

    }
}