package com.revolgenx.anilib.infrastructure.source.home.discover

import android.content.Context
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.extensions.HeaderSource
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.model.CommonMediaModel
import com.revolgenx.anilib.infrastructure.source.MediaSource

class MediaFormatHeaderSource(context: Context) : HeaderSource<CommonMediaModel, String>() {

    private val mediaFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }
    private var lastHeader: String = ""
    override fun areItemsTheSame(
        first: Data<CommonMediaModel, String>,
        second: Data<CommonMediaModel, String>
    ): Boolean {
        return first.anchor.mediaId == second.anchor.mediaId
    }

    override fun dependsOn(source: Source<*>): Boolean {
        return source is MediaSource
    }

    override fun computeHeaders(
        page: Page,
        list: List<CommonMediaModel>
    ): List<Data<CommonMediaModel, String>> {
        val results = arrayListOf<Data<CommonMediaModel, String>>()

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