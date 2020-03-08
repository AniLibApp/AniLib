package com.revolgenx.anilib.fragment.browser

import com.revolgenx.anilib.R
import com.revolgenx.anilib.fragment.base.BaseToolbarFragment

class MediaStaffFragment :BaseToolbarFragment(){
    override val title: String by lazy {
        getString(R.string.staff)
    }

    override val contentRes: Int
        get() = R.layout.media_staff_fragment_layout
}
