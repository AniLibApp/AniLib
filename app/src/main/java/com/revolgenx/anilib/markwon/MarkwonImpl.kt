package com.revolgenx.anilib.markwon

import android.content.Context
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin

object MarkwonImpl {
    lateinit var instanceHtml: Markwon
    lateinit var instance: Markwon

    fun createHtmlInstance(context: Context) {
        instanceHtml = Markwon.builder(context)
            .usePlugin(HtmlPlugin.create())
            .build()
    }

    fun createInstance(context: Context) {
        instance = Markwon.builder(context).build()
    }


}