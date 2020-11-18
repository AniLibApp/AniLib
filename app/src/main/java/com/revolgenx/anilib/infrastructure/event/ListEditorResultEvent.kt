package com.revolgenx.anilib.infrastructure.event

import com.revolgenx.anilib.data.meta.ListEditorResultMeta
data class ListEditorResultEvent(var listEditorResultMeta: ListEditorResultMeta) : CommonEvent()