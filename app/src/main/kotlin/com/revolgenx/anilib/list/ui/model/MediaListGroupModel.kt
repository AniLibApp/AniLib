package com.revolgenx.anilib.list.ui.model

data class MediaListGroupModel(
    val entries: MutableList<MediaListModel>? = null,
    val name: String? = null,
    val isCustomList: Boolean = false,
    val isCompletedList: Boolean = false,
    val status: Int? = null,
    val count: Int = entries?.count() ?: 0,
    val order: Int = -1
)