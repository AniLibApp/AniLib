package com.revolgenx.anilib.infrastructure.event

import com.revolgenx.anilib.entry.data.meta.EntryEditorResultMeta
data class ListEditorResultEvent(var listEditorResultMeta: EntryEditorResultMeta) : CommonEvent()