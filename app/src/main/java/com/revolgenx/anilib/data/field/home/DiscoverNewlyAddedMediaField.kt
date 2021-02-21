package com.revolgenx.anilib.data.field.home

import android.content.Context
import com.revolgenx.anilib.data.field.media.MediaField
import com.revolgenx.anilib.common.preference.getNewlyAddedField
import com.revolgenx.anilib.common.preference.storeNewlyAddedField

class NewlyAddedMediaField : MediaField() {
    companion object {
        fun create(context: Context) = getNewlyAddedField(context)
    }

    override var includeStaff: Boolean = true
    override var includeStudio: Boolean = true

    fun saveNewlyAddedField(context: Context) {
        storeNewlyAddedField(context, this)
    }
}
