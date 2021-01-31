package com.revolgenx.anilib.ui.fragment.home.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.DiscoverContainerFragmentBinding
import com.revolgenx.anilib.infrastructure.event.BrowseEvent
import com.revolgenx.anilib.infrastructure.event.BrowseNotificationEvent
import com.revolgenx.anilib.ui.fragment.home.discover.DiscoverFragment
import com.revolgenx.anilib.ui.fragment.home.recommendation.RecommendationFragment
import com.revolgenx.anilib.ui.fragment.home.season.SeasonFragment

class DiscoverContainerFragment : BaseLayoutFragment<DiscoverContainerFragmentBinding>() {

    private lateinit var adapter: FragmentPagerAdapter

    private val discoverFragments by lazy {
        listOf(
            DiscoverFragment(),
            SeasonFragment(),
            RecommendationFragment()
        )
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): DiscoverContainerFragmentBinding {
        return DiscoverContainerFragmentBinding.inflate(inflater, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.initListener()

        binding.discoverNotificationIv.visibility = if(requireContext().loggedIn()) View.VISIBLE else View.GONE

        adapter = makePagerAdapter(
            discoverFragments,
            resources.getStringArray(R.array.discover_tab_menu)
        )
        binding.discoverContainerViewPager.adapter = adapter
        binding.discoverContainerViewPager.offscreenPageLimit = 3
        binding.discoverMainTabLayout.setupWithViewPager(binding.discoverContainerViewPager)

    }

    private fun DiscoverContainerFragmentBinding.initListener(){
        discoverSearchIv.setOnClickListener {
            BrowseEvent().postEvent
        }
        discoverNotificationIv.setOnClickListener {
            BrowseNotificationEvent().postEvent
        }
    }

}