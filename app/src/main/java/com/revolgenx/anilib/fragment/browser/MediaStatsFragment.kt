package com.revolgenx.anilib.fragment.browser

import com.revolgenx.anilib.R
import com.revolgenx.anilib.fragment.base.BaseToolbarFragment

class MediaStatsFragment:BaseToolbarFragment(){
    override val title: String by lazy {
        getString(R.string.stats)
    }

    override val contentRes: Int
        get() = R.layout.media_stats_fragment_layout
}
