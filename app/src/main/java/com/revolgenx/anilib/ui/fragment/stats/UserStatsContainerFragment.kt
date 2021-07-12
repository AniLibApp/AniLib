package com.revolgenx.anilib.ui.fragment.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentPagerAdapter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.constant.UserConstant
import com.revolgenx.anilib.data.meta.UserStatsMeta
import com.revolgenx.anilib.databinding.UserStatsContainerFragmentLayoutBinding
import com.revolgenx.anilib.type.MediaType

class UserStatsContainerFragment : BaseLayoutFragment<UserStatsContainerFragmentLayoutBinding>() {
    companion object {
        fun newInstance(userStatsMeta: UserStatsMeta) = UserStatsContainerFragment().also {
            it.arguments = bundleOf(UserConstant.USER_STATS_META_KEY to userStatsMeta)
        }
    }


    private val userStatsMeta get()= arguments?.getParcelable<UserStatsMeta?>(UserConstant.USER_STATS_META_KEY)

    private val userAnimeStatsFragments by lazy {
        listOf(
            StatsOverviewFragment(),
            StatsGenreFragment(),
            StatsTagFragment(),
            StatsVoiceActorFragment(),
            StatsStudioFragment(),
            StatsStaffFragment()
        )
    }

    private val userMangaStatsFragments by lazy {
        listOf(
            StatsOverviewFragment(),
            StatsGenreFragment(),
            StatsTagFragment(),
            StatsStaffFragment()
        )
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): UserStatsContainerFragmentLayoutBinding {
        return UserStatsContainerFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val userStats = userStatsMeta ?: return

        val adapter: FragmentPagerAdapter

        when (userStats.type) {
            MediaType.ANIME.ordinal -> {
                userAnimeStatsFragments.forEach {
                    it.arguments = bundleOf(
                        UserConstant.USER_STATS_META_KEY to userStats
                    )
                }

                adapter = makePagerAdapter(
                    userAnimeStatsFragments,
                    resources.getStringArray(R.array.user_anime_stats_tab_menu)
                )
            }
            else -> {
                userMangaStatsFragments.forEach {
                    it.arguments = bundleOf(
                        UserConstant.USER_STATS_META_KEY to userStats
                    )
                }
                adapter = makePagerAdapter(
                    userMangaStatsFragments,
                    resources.getStringArray(R.array.user_manga_stats_tab_menu)
                )
            }
        }

        binding.userStatsContainerViewPager.adapter = adapter
        binding.userStatsContainerViewPager.offscreenPageLimit = adapter.count - 1
        binding.userStatsTabLayout.setupWithViewPager(binding.userStatsContainerViewPager)
    }
}