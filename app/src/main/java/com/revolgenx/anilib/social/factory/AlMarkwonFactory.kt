package com.revolgenx.anilib.social.factory

import android.annotation.SuppressLint
import android.content.Context
import com.revolgenx.anilib.social.markwon.AlMarkwon
import com.revolgenx.anilib.social.markwon.AlMarkwonCallback

@SuppressLint("StaticFieldLeak")
object AlMarkwonFactory {
    private lateinit var alMarkwonCallbackImpl: AlMarkwonCallback
    private lateinit var mContext: Context

    fun init(context: Context) {
        this.mContext = context
        this.alMarkwonCallbackImpl = AlMarkwonCallbackImpl(context)
    }

    fun getMarkwon() = AlMarkwon.getMarkwon(mContext, alMarkwonCallbackImpl)

}