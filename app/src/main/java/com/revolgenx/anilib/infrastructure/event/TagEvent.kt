package com.revolgenx.anilib.infrastructure.event

import com.revolgenx.anilib.constant.MediaTagFilterTypes
import com.revolgenx.anilib.common.data.field.TagField

data class TagEvent(
    val tagType: MediaTagFilterTypes,
    val tagFields: List<TagField>
) : BaseEvent()

