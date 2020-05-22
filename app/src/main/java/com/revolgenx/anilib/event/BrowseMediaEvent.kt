package com.revolgenx.anilib.event

import android.view.View
import com.revolgenx.anilib.meta.MediaBrowserMeta

data class BrowseMediaEvent(var mediaBrowserMeta: MediaBrowserMeta, var sharedElement: View?) : CommonEvent()