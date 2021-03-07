package com.revolgenx.anilib.infrastructure.event

import com.revolgenx.anilib.data.meta.MediaListFilterMeta

sealed class ListEvent : BaseEvent() {
    data class ListStatusChangedEvent(val listType: Int, val status: Int) : ListEvent()
    data class ListFilterChangedEvent(val listType: Int, val meta: MediaListFilterMeta) :ListEvent()
}
