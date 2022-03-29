package com.revolgenx.anilib.home.discover.data.field

import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.common.preference.getTrendingField
import com.revolgenx.anilib.common.preference.storeTrendingField

class TrendingMediaField : MediaField() {

    override var includeStaff: Boolean = true
    override var includeStudio: Boolean = true

    companion object {
        fun create() = getTrendingField()
    }

    fun saveTrendingField() {
        storeTrendingField(this)
    }
}