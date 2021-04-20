package com.revolgenx.anilib.infrastructure.event

import android.view.View
import androidx.annotation.IdRes
import com.revolgenx.anilib.data.meta.ListEditorMeta

data class ListEditorEvent(val meta: ListEditorMeta, val sharedElement: View? = null, val openInWithSupportFragment:Boolean = false, @IdRes val containerId:Int = 0):CommonEvent()
