package com.revolgenx.anilib.event

import com.revolgenx.anilib.meta.ListEditorResultMeta
data class ListEditorResultEvent(var listEditorResultMeta: ListEditorResultMeta) : CommonEvent()