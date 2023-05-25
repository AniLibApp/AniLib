package com.revolgenx.anilib.social.factory

import android.annotation.SuppressLint
import android.content.Context
import com.revolgenx.anilib.social.markwon.AlMarkwon

@SuppressLint("StaticFieldLeak")
object AlMarkwonFactory {
    fun init(context: Context, alMarkwonCallback: AlMarkwonCallback) {
        AlMarkwon.init(context, alMarkwonCallback, AlMarkwonPluginsProviderImpl())
    }
}