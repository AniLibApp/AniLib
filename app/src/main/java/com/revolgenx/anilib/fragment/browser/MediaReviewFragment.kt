package com.revolgenx.anilib.fragment.browser

import com.revolgenx.anilib.R
import com.revolgenx.anilib.fragment.base.BaseToolbarFragment

class MediaReviewFragment:BaseToolbarFragment() {

    override val title: String by lazy {
        getString(R.string.review)
    }
    override val contentRes: Int
        get() = R.layout.media_review_fragment_layout
}
