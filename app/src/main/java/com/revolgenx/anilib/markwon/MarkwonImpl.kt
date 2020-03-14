package com.revolgenx.anilib.markwon

import android.content.Context
import io.noties.markwon.Markwon

object MarkwonImpl {
    lateinit var instance: Markwon

    fun createInstance(context: Context) {
        instance = Markwon.create(context)
    }
}