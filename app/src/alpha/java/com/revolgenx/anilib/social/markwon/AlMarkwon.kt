package com.revolgenx.anilib.social.markwon

import android.content.Context
import com.revolgenx.anilib.social.factory.AlMarkwonCallback
import com.revolgenx.anilib.social.factory.AlMarkwonPluginsProvider
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.linkify.LinkifyPlugin

object AlMarkwon {
    var _markwon: Markwon? = null
        private set
    val markwon: Markwon get() = _markwon!!

    fun init(
        context: Context,
        alMarkwonCallback: AlMarkwonCallback,
        alMarkwonPluginsProvider: AlMarkwonPluginsProvider? = null
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