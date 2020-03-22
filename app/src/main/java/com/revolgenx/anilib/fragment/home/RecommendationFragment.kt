package com.revolgenx.anilib.fragment.home

import android.view.Menu
import android.view.MenuInflater
import com.revolgenx.anilib.R
import com.revolgenx.anilib.fragment.base.BaseFragment

class RecommendationFragment :BaseFragment(){

    override fun onResume() {
        super.onResume()
        invalidateOptionMenu()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_menu, menu)
    }
}
