package com.revolgenx.anilib.app.preference

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import com.revolgenx.anilib.common.data.store.appPreferenceDataStore
import com.revolgenx.anilib.media.ui.model.MediaTitleType
import kotlinx.coroutines.flow.map

val mediaTitleKey = intPreferencesKey("media_title_key")

fun Context.mediaTitleType() = this.appPreferenceDataStore.data.map {
    MediaTitleType.values()[it[mediaTitleKey] ?: 0]
}