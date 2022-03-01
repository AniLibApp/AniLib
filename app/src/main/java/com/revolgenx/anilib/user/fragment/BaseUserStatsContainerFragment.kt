package com.revolgenx.anilib.user.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.adapter.makeViewPagerAdapter2
import com.revolgenx.anilib.common.ui.adapter.setupWithViewPager2
import com.revolgenx.anilib.databinding.UserStatsContainerFragmentLayoutBinding
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.user.viewmodel.UserStatsContainerSharedVM
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseUserStatsContainerFragment :
    BaseUserFragment<UserStatsContainerFragmentLayoutBinding>() {
    protected open val type: Int = MediaType.ANIME.ordinal

    private val viewModel by viewModel<UserStatsContainerSharedVM>()
    private val userAnimeStatsFragments by lazy {
        listOf(
            AnimeStatisticOverViewFragment(),
            AnimeUserStatsGenreFragment(),
            AnimeUserStatsTagFragment(),
            UserStatsVoiceActorFragment(),
            UserStatsStudioFragment(),
            AnimeUserStatsStaffFragment()
        )
    }

    private val userMangaStatsFragments by lazy {
        listOf(
            MangaStatisticOverViewFragment(),
            MangaUserStatsGenreFragment(),
            MangaUserStatsTagFragment(),
            MangaUserStatsStaffFragment()
        )
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): UserStatsContainerFragmentLayoutBinding {
        return UserStatsContainerFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!sharedViewModel.hasUserData) return

        if(savedInstanceState == null) {
            with(viewModel) {
                userId = sharedViewModel.userId
                userName = sharedViewModel.userName
            }
        }

        val adapter: FragmentStateAdapter
        val tabItems: Array<String>

        when (type) {
            MediaType.ANIME.ordinal -> {
                adapter = makeViewPagerAdapter2(
                    userAnimeStatsFragments
                )
                tabItems = resources.getStringArray(R.array.user_anime_stats_tab_menu)
            }
            else -> {
                adapter = makeViewPagerAdapter2(
                    userMangaStatsFragments
                )
                tabItems = resources.getStringArray(R.array.user_manga_stats_tab_menu)
            }
        }

        binding.userStatsContainerViewPager.adapter = adapter
        setupWithViewPager2(
            binding.userStatsTabLayout,
            binding.userStatsContainerViewPager,
            tabItems
        )
        binding.userStatsContainerViewPager.offscreenPageLimit = tabItems.size - 1
    }
}