package com.revolgenx.anilib.field.home

import android.content.Context
import com.revolgenx.anilib.field.media.MediaField
import com.revolgenx.anilib.preference.getNewlyAddedField
import com.revolgenx.anilib.preference.storeNewlyAddedField

class NewlyAddedMediaField : MediaField() {
    companion object {
        fun create(context: Context) = getNewlyAddedField(context)
    }

    fun saveNewlyAddedField(context: Context) {
        storeNewlyAddedField(context, this)
    }
}
