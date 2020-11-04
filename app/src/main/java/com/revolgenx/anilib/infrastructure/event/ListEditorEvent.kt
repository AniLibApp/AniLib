package com.revolgenx.anilib.infrastructure.event

import android.view.View
import com.revolgenx.anilib.data.meta.ListEditorMeta

data class ListEditorEvent(var meta: ListEditorMeta, var sharedElement: View):CommonEvent()
