package com.revolgenx.anilib.markwon

import android.content.Context
import com.revolgenx.anilib.view.plugins.SpoilerPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin

object MarkwonImpl {
    lateinit var instanceHtml: Markwon

    fun createHtmlInstance(context: Context) {
        instanceHtml = Markwon.builder(context)
            .usePlugin(HtmlPlugin.create())
            .usePlugin(SpoilerPlugin.create())
            .build()
    }
}