package com.revolgenx.anilib.home.discover.data.field

import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.common.preference.getNewlyAddedField
import com.revolgenx.anilib.common.preference.storeNewlyAddedField

class NewlyAddedMediaField : MediaField() {
    companion object {
        fun create() = getNewlyAddedField()
    }

    override var includeStaff: Boolean = true
    override var includeStudio: Boolean = true

    fun saveNewlyAddedField() {
        storeNewlyAddedField(this)
    }
}
