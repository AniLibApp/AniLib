package com.revolgenx.anilib.fragment

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.revolgenx.anilib.R
import com.revolgenx.anilib.fragment.base.BaseLayoutFragment
import com.revolgenx.anilib.util.copyToClipBoard
import kotlinx.android.synthetic.main.log_fragment_layout.*

class LogFragment : BaseLayoutFragment() {
    override val layoutRes: Int = R.layout.log_fragment_layout
    override var setHomeAsUp: Boolean = true


    override fun onStart() {
        super.onStart()
        logView.start()
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.log_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.copyLog -> {
                context?.copyToClipBoard(logView.sb.toString())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        super.onStop()
        logView.stop()
    }
}