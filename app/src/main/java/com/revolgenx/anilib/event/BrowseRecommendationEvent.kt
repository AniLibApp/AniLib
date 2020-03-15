package com.revolgenx.anilib.event

import android.view.View
import com.revolgenx.anilib.event.meta.MediaBrowserMeta

class BrowseRecommendationEvent(var mediaBrowserMeta: MediaBrowserMeta, var sharedElement: View) :BaseEvent()