package com.revolgenx.anilib.meta

import kotlinx.android.parcel.Parcelize

@Parcelize
data class TagFilterSettingMeta(var type: TagFilterMetaType) : BaseMeta

enum class TagFilterMetaType {
    GENRE, TAG, STREAMING_ON
}