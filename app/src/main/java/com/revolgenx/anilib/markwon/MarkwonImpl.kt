package com.revolgenx.anilib.markwon

import android.content.Context
import com.revolgenx.anilib.plugins.FrescoImagePlugin
import com.revolgenx.anilib.plugins.SpoilerPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import timber.log.Timber

object MarkwonImpl {
    lateinit var instanceHtml: Markwon

    fun createHtmlInstance(context: Context):Markwon {
        Timber.d("created html markdown instance")
        instanceHtml = Markwon.builder(context)
            .usePlugin(HtmlPlugin.create())
            .usePlugin(SpoilerPlugin.create())
            .usePlugin(FrescoImagePlugin.create(context))
            .build()
        return instanceHtml
    }
}