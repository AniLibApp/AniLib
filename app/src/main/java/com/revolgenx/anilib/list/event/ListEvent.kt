package com.revolgenx.anilib.list.event

import com.revolgenx.anilib.common.event.BaseEvent
import com.revolgenx.anilib.list.data.model.MediaListModel

sealed class ListEvent : BaseEvent() {
    data class ListAddEvent(val list: MediaListModel) : ListEvent()
    data class ListUpdateEvent(val list: MediaListModel) : ListEvent()
    data class ListDeleteEvent(val id: Int) : ListEvent()
}