package com.revolgenx.anilib.infrastructure.event

import com.revolgenx.anilib.ui.selector.constant.SelectableTypes
import com.revolgenx.anilib.common.data.field.TagField

data class TagEvent(
    val tagType: SelectableTypes,
    val tagFields: List<TagField>
) : BaseEvent()

