package com.revolgenx.anilib.social.factory

import android.content.Context
import com.revolgenx.anilib.social.markwon.AlMarkwon

object AlMarkwonFactory {
    private var alMarkwonCallbackImpl: AlMarkwonCallbackImpl? = null
    private var mContext: Context? = null
    fun init(context: Context) {
        this.mContext = context
        this.alMarkwonCallbackImpl = AlMarkwonCallbackImpl()
    }

    fun getMarkwon() = AlMarkwon.getMarkwon(mContext!!, alMarkwonCallbackImpl!!)
}