package com.revolgenx.anilib.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.pranavpandey.android.dynamic.support.widget.DynamicToolbar
import com.revolgenx.anilib.R
import kotlinx.android.synthetic.main.base_toolbar_fragment_layout.view.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*

abstract class BaseToolbarFragment : BaseFragment() {
    var setHomeAsUp = true
    var subtitle: String? = null

    lateinit var toolbar:DynamicToolbar
    abstract val title: Int
    abstract val contentRes: Int


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.base_toolbar_fragment_layout, container, false)
        toolbar = v.dynamicToolbar
        val content = inflater.inflate(contentRes, container, false)
        v.toolbarContainerLayout.addView(content)
        return v
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionMenu()
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).also {
            it.setSupportActionBar(toolbar)
            it.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            it.supportActionBar!!.title = getString(title)
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