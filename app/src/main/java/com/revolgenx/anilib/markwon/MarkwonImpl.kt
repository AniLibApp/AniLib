package com.revolgenx.anilib.markwon

import android.content.Context
import com.revolgenx.anilib.plugins.*
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import timber.log.Timber


object MarkwonImpl {
    lateinit var instanceHtml: Markwon

    fun createHtmlInstance(context: Context): Markwon {
        Timber.d("created html markdown instance")
        if (!::instanceHtml.isInitialized)
            instanceHtml = Markwon.builder(context)
                .usePlugin(HtmlPlugin.create())
                .usePlugin(ImageTagPlugin())
                .usePlugin(SpoilerPlugin.create())
                .usePlugin(FrescoImagePlugin.create(context))
//                .usePlugin(ImageClickHandlerPlugin())
                .usePlugin(VideoTagPlugin(context))
                .usePlugin(YoutubeTagPlugin(context))
                .build()
        return instanceHtml
    }
}