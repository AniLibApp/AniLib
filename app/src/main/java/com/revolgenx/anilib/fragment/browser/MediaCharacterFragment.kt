package com.revolgenx.anilib.fragment.browser

import com.revolgenx.anilib.R
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.fragment.base.BaseToolbarFragment

class MediaCharacterFragment:BaseToolbarFragment() {
    override val title: String by lazy {
        getString(R.string.character)
    }

    override val contentRes: Int
        get() = R.layout.media_character_fragment_layout
}

