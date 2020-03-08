package com.revolgenx.anilib.event

import android.view.View
import com.revolgenx.anilib.event.meta.MediaBrowserMeta

data class BrowseMediaEvent(var mediaBrowserMeta: MediaBrowserMeta, var sharedElement: View) : BaseEvent()