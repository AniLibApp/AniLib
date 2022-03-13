package com.revolgenx.anilib.home.discover.data.field

import android.content.Context
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.common.preference.getNewlyAddedField
import com.revolgenx.anilib.common.preference.storeNewlyAddedField

class NewlyAddedMediaField : MediaField() {
    companion object {
        fun create() = getNewlyAddedField()
    }

    override var includeStaff: Boolean = true
    override var includeStudio: Boolean = true

    fun saveNewlyAddedField(context: Context) {
        storeNewlyAddedField(context, this)
    }
}
