package com.revolgenx.anilib.infrastructure.event

import android.view.View
import com.revolgenx.anilib.data.meta.MediaBrowserMeta

data class BrowseMediaEvent(var mediaBrowserMeta: MediaBrowserMeta, var sharedElement: View?) : CommonEvent()