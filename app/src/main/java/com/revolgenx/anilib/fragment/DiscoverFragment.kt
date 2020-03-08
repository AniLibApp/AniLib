package com.revolgenx.anilib.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import com.revolgenx.anilib.R
import com.revolgenx.anilib.fragment.base.BaseFragment

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
        inflater.inflate(R.menu.list_editor_menu, menu)
    }

}
