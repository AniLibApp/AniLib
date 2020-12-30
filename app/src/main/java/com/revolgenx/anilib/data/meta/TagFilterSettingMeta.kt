package com.revolgenx.anilib.data.meta

import kotlinx.parcelize.Parcelize

@Parcelize
data class TagFilterSettingMeta(var type: TagFilterMetaType) : BaseMeta

enum class TagFilterMetaType {
    GENRE, TAG, STREAMING_ON
}