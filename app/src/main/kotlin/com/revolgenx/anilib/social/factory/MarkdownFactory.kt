package com.revolgenx.anilib.social.factory

import android.content.Context
import io.noties.markwon.Markwon

interface MarkdownFactory {
    fun initialize(
        context: Context,
        primaryColor: Int,
        autoPlayGif: Boolean,
        markdownCallback: MarkdownCallback
    ): Markwon
    fun anilify(text: String?): String
    fun destroy()
}