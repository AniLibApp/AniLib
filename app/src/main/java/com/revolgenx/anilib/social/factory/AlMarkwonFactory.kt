package com.revolgenx.anilib.social.factory

import android.annotation.SuppressLint
import android.content.Context
import com.revolgenx.anilib.social.markwon.AlMarkwonCallback
import io.noties.markwon.Markwon
import timber.log.Timber

@SuppressLint("StaticFieldLeak")
object AlMarkwonFactory {
    private var alMarkwonCallbackImpl: AlMarkwonCallbackImpl? = null
    private var mContext: Context? = null
    fun init(context: Context) {
        this.mContext = context
        this.alMarkwonCallbackImpl = AlMarkwonCallbackImpl(context)
    }

    fun getMarkwon(context:Context?=null) = getAlMarkwon(context ?: mContext!!) ?: getDefaultMarkwon(context ?: mContext!!) //AlMarkwon.getMarkwon(context ?: mContext!!, alMarkwonCallbackImpl!!)

    private fun getAlMarkwon(context: Context):Markwon?{
        try{
            val alMarkwon = Class.forName("com.revolgenx.anilib.social.markwon.AlMarkwon")
            val alMarkwonKClass = alMarkwon.kotlin
            return alMarkwon.getMethod("getMarkwon", Context::class.java, AlMarkwonCallback::class.java).invoke(alMarkwonKClass.objectInstance, context, alMarkwonCallbackImpl) as Markwon
        }catch(e:Exception){
            Timber.e(e)
        }
        return null
    }

    private fun getDefaultMarkwon(context: Context):Markwon= Markwon.create(context)
}