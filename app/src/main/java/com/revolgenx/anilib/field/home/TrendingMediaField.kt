package com.revolgenx.anilib.field.home

import android.content.Context
import com.revolgenx.anilib.field.media.MediaField
import com.revolgenx.anilib.preference.getTrendingField
import com.revolgenx.anilib.preference.storeTrendingField

class TrendingMediaField : MediaField() {
    companion object {
        fun create(context: Context) = getTrendingField(context)
    }

    fun saveTrendingField(context: Context) {
        storeTrendingField(context, this)
    }
}