package com.revolgenx.anilib.fragment.home

import android.content.Context
import android.os.Bundle
import android.view.*
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.util.makeSnakeBar
import kotlinx.android.synthetic.main.discover_fragment_layout.*
import kotlinx.android.synthetic.main.overview_recommendation_presnter_layout.*

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loading.color = DynamicTheme.getInstance().get().accentColor

        mediaRecommendationDislikeTv.setOnClickListener {
            it.checkLoggedIn()
        }
    }


    private fun View.checkLoggedIn(): Boolean {
        val loggedIn = context.loggedIn()
        if (!loggedIn) makeSnakeBar(R.string.please_log_in)
        return loggedIn
    }

}
