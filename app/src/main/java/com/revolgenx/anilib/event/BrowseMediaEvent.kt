package com.revolgenx.anilib.event

import android.view.View
import com.revolgenx.anilib.event.meta.BrowseMediaMeta

data class BrowseMediaEvent(var browseMediaMeta: BrowseMediaMeta, var sharedElement: View) : BaseEvent()