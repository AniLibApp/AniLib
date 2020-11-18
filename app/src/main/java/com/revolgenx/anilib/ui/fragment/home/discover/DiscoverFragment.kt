package com.revolgenx.anilib.ui.fragment.home.discover

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.revolgenx.anilib.R
import com.revolgenx.anilib.ui.dialog.HomeOrderDialog
import com.revolgenx.anilib.infrastructure.event.BrowseEvent
import com.revolgenx.anilib.infrastructure.event.BrowseNotificationEvent
import com.revolgenx.anilib.common.preference.loggedIn

class DiscoverFragment : DiscoverReviewFragment() {

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
            R.id.searchMenu -> {
                BrowseEvent().postEvent
                true
            }
            R.id.notificationMenu -> {
                BrowseNotificationEvent().postEvent
                true
            }
            R.id.orderMenu->{
                HomeOrderDialog().show(childFragmentManager, HomeOrderDialog::class.java.simpleName)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
