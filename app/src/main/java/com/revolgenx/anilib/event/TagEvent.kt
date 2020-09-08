package com.revolgenx.anilib.event

import com.revolgenx.anilib.constant.MediaTagFilterTypes
import com.revolgenx.anilib.field.TagField

data class TagEvent(
    val tagType: MediaTagFilterTypes,
    val tagFields: List<TagField>
) : BaseEvent()

