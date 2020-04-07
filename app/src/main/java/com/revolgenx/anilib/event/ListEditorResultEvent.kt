package com.revolgenx.anilib.event

import com.revolgenx.anilib.event.meta.ListEditorResultMeta
data class ListEditorResultEvent(var listEditorResultMeta: ListEditorResultMeta) : BaseEvent()