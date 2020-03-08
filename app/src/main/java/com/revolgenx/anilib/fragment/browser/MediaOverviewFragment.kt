package com.revolgenx.anilib.fragment.browser

import android.os.Bundle
import android.view.*
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.MediaBrowserActivity
import com.revolgenx.anilib.event.meta.MediaBrowserMeta
import com.revolgenx.anilib.fragment.base.BaseFragment

class MediaOverviewFragment : BaseFragment() {

    private var mediaBrowserMeta: MediaBrowserMeta? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.media_overview_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mediaBrowserMeta = arguments?.getParcelable(MediaBrowserActivity.MEDIA_BROWSER_META) ?: return
    }



}