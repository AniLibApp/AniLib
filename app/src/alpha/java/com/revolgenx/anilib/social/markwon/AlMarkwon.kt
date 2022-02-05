package com.revolgenx.anilib.social.markwon

import android.content.Context
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.linkify.LinkifyPlugin

object AlMarkwon {
    private var _markwon: Markwon? = null
    fun getMarkwon(context: Context, alMarkwonCallback: AlMarkwonCallback): Markwon {
        return if (_markwon == null) {
            Markwon.builder(context)
                .usePlugins(
                    listOf(
                        HtmlPlugin.create(),
                        StrikethroughPlugin.create(),
                        LinkifyPlugin.create()
                    )
                ).build()
        } else {
            _markwon!!
        }
    }
}