package com.revolgenx.anilib.field.home

import android.content.Context
import com.revolgenx.anilib.field.media.MediaField
import com.revolgenx.anilib.preference.getPopularField
import com.revolgenx.anilib.preference.storePopularField

class PopularMediaField: MediaField() {
    companion object {
        fun create(context: Context) = getPopularField(context)
    }

    fun savePopularField(context: Context) {
        storePopularField(context, this)
    }
}