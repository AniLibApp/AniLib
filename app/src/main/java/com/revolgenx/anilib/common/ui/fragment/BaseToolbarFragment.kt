package com.revolgenx.anilib.common.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.pranavpandey.android.dynamic.support.widget.DynamicToolbar
import com.revolgenx.anilib.databinding.BaseToolbarFragmentLayoutBinding

abstract class BaseToolbarFragment<T:ViewBinding> : BaseLayoutFragment<T>() {
    var subtitle: String? = null

    lateinit var toolbar:DynamicToolbar
    abstract val title: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val b = BaseToolbarFragmentLayoutBinding.inflate(inflater, container, false)
        toolbar = b.toolbarLayout.dynamicToolbar
        val content =  super.onCreateView(inflater, container, savedInstanceState)
        b.toolbarContainerLayout.addView(content)
        return b.root
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