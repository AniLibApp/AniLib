package com.revolgenx.anilib.social.factory

import android.content.Context
import com.revolgenx.anilib.social.markdown.AlMarkdown
import io.noties.markwon.Markwon


object MarkdownFactoryImpl : MarkdownFactory {
    private var _markwon: Markwon? = null
    val markwon: Markwon get() = _markwon!!

    override fun initialize(
        context: Context,
        primaryColor: Int,
        autoPlayGif: Boolean,
        markdownCallback: MarkdownCallback
    ): Markwon {
        _markwon = AlMarkdown.create(context, primaryColor, autoPlayGif, markdownCallback)
        return _markwon!!
    }

    override fun anilify(text: String?): String {
        return AlMarkdown.anilify(text)
    }

}