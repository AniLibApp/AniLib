package com.revolgenx.anilib.social.markdown

import android.content.Context
import com.revolgenx.anilib.social.factory.AlMarkdownPluginsProvider
import com.revolgenx.anilib.social.factory.AlMarkdownCallback
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.linkify.LinkifyPlugin

object AlMarkdown {
    var _markwon: Markwon? = null
        private set
    val markwon: Markwon get() = _markwon!!

    fun init(
        context: Context,
        callback: AlMarkdownCallback,
        alMarkdownPluginsProvider: AlMarkdownPluginsProvider? = null
    ): Markwon {
        if (_markwon == null) {
            Markwon.builder(context)
                .usePlugins(
                    listOf(
                        HtmlPlugin.create(),
                        StrikethroughPlugin.create(),
                        LinkifyPlugin.create()
                    )
                ).build()
        }
        return markwon
    }
}