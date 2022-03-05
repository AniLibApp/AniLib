package com.revolgenx.anilib.home.discover.fragment

import android.os.Bundle
import android.view.View


class DiscoverFragment : DiscoverReviewFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.discoverSwipeToRefreshLayout.setOnRefreshListener {
            binding.discoverSwipeToRefreshLayout.isRefreshing = false
            reloadContent()
        }

    }
}
