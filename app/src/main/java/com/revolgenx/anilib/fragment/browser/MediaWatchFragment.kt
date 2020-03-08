package com.revolgenx.anilib.fragment.browser

import com.revolgenx.anilib.R
import com.revolgenx.anilib.fragment.base.BaseToolbarFragment

class MediaWatchFragment :BaseToolbarFragment(){

    override val title: String by lazy {
        getString(R.string.watch)
    }

    override val contentRes: Int
        get() = R.layout.media_watch_fragment_layout
}
