package com.revolgenx.anilib.event

import android.view.View
import com.revolgenx.anilib.event.meta.ListEditorMeta

data class ListEditorEvent(var meta: ListEditorMeta, var sharedElement: View):BaseEvent()
