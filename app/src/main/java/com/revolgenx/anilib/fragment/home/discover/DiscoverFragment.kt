package com.revolgenx.anilib.fragment.home.discover

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.ContainerActivity
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.fragment.base.ParcelableFragment
import com.revolgenx.anilib.fragment.home.NotificationFragment
import com.revolgenx.anilib.meta.UserMeta
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.preference.userId
import com.revolgenx.anilib.util.makeSnakeBar

class DiscoverFragment : DiscoverNewFragment() {

    override fun onResume() {
        super.onResume()
        invalidateOptionMenu()
        setHasOptionsMenu(true)
        updateToolbarTitle()
    }

    private fun updateToolbarTitle() {
        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.setTitle(R.string.app_name)
            it.supportActionBar?.setSubtitle(0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.discover_fragment_menu, menu)
        menu.findItem(R.id.notificationMenu).isVisible = requireContext().loggedIn()
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
