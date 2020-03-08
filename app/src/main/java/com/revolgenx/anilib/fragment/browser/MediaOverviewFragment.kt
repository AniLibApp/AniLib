package com.revolgenx.anilib.fragment.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.MediaBrowserActivity
import com.revolgenx.anilib.event.meta.BrowseMediaMeta
import com.revolgenx.anilib.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.media_overview_fragment.*

class MediaOverviewFragment : BaseFragment() {

    private var mediaMeta: BrowseMediaMeta? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.media_overview_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionMenu()
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mediaMeta = arguments?.getParcelable(MediaBrowserActivity.MEDIA_BROWSER_META) ?: return

        mediaOverviewCoverImage.setImageURI(mediaMeta!!.coverImage)
        mediaOverviewBannerImage.setImageURI(mediaMeta!!.coverImage)

        (activity as AppCompatActivity).also { act ->
            act.setSupportActionBar(mediaOverviewToolbar)
            act.supportActionBar!!.title = mediaMeta!!.title
            act.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

}