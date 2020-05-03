package com.revolgenx.anilib.fragment.home

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.ContainerActivity
import com.revolgenx.anilib.activity.NavViewPagerContainerActivity
import com.revolgenx.anilib.event.BrowseEvent
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.fragment.base.ParcelableFragment
import com.revolgenx.anilib.markwon.MarkwonImpl
import com.revolgenx.anilib.meta.NavViewPagerContainerMeta
import com.revolgenx.anilib.meta.NavViewPagerContainerType
import com.revolgenx.anilib.meta.UserMeta
import com.revolgenx.anilib.meta.UserStatsMeta
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.preference.userId
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.util.makeSnakeBar
import com.revolgenx.anilib.view.drawable.VideoPlayBitmapDrawable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.discover_fragment_layout.*

class DiscoverFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.discover_fragment_layout, container, false)
    }


    override fun onResume() {
        super.onResume()
        invalidateOptionMenu()
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.discover_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.notificationMenu -> {
                ContainerActivity.openActivity(
                    requireContext(),
                    ParcelableFragment(
                        NotificationFragment::class.java,
                        bundleOf(
                            UserMeta.userMetaKey to UserMeta(
                                requireContext().userId(),
                                null,
                                true
                            )
                        )
                    )
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    private fun View.checkLoggedIn(): Boolean {
        val loggedIn = context.loggedIn()
        if (!loggedIn) makeSnakeBar(R.string.please_log_in)
        return loggedIn
    }

}
