package com.revolgenx.anilib.social.factory

import android.annotation.SuppressLint
import android.content.Context
import com.revolgenx.anilib.social.markdown.AlMarkdown

@SuppressLint("StaticFieldLeak")
object AlMarkdownFactory {
    fun init(context: Context, alMarkdownCallback: AlMarkdownCallback) {
        AlMarkdown.init(context, alMarkdownCallback)
    }
}