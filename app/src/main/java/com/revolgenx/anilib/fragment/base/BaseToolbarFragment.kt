package com.revolgenx.anilib.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.revolgenx.anilib.R
import kotlinx.android.synthetic.main.base_toolbar_fragment_layout.view.*
import kotlinx.android.synthetic.main.toolbar_layout.*

abstract class BaseToolbarFragment : BaseFragment() {
    var setHomeAsUp = true
    var subtitle: String? = null
    abstract val title: String
    abstract val contentRes: Int


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.base_toolbar_fragment_layout, container, false)
        val content = inflater.inflate(contentRes, container, false)
        v.toolbarContainerLayout.addView(content)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).also {
            it.setSupportActionBar(dynamicToolbar)
            it.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            it.supportActionBar!!.title = title
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finishActivity()
                true
            }
            else -> false
        }
    }
}