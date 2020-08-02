package com.revolgenx.anilib.event

import com.revolgenx.anilib.field.TagField

data class TagEvent(
    val operationType: TagOperationType,
    val tag: String?,
    val tagFields: List<TagField>
) : BaseEvent()

enum class TagOperationType {
    ADD_TAG,
    DELETE_TAG,
    ADD_GENRE,
    DELETE_GENRE,
    ADD_STREAM,
    DELETE_STREAM
}