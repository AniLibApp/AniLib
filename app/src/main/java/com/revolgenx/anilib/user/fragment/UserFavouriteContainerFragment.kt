package com.revolgenx.anilib.user.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.adapter.makeViewPagerAdapter2
import com.revolgenx.anilib.common.ui.adapter.setupWithViewPager2
import com.revolgenx.anilib.databinding.UserFavouriteContainerFragmentLayoutBinding
import com.revolgenx.anilib.user.viewmodel.UserFavouriteContainerSharedVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFavouriteContainerFragment :
    BaseUserFragment<UserFavouriteContainerFragmentLayoutBinding>() {

    private val userFavouriteFragments by lazy {
        listOf(
            AnimeFavouriteFragment(),
            MangaFavouriteFragment(),
            CharacterFavouriteFragment(),
            StaffFavouriteFragment(),
            StudioFavouriteFragment()
        )
    }

    private val viewModel by viewModel<UserFavouriteContainerSharedVM>()

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): UserFavouriteContainerFragmentLayoutBinding {
        return UserFavouriteContainerFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!sharedViewModel.hasUserData) return

        with(viewModel) {
            userName = sharedViewModel.userName
            userId = sharedViewModel.userId
        }

        binding.userFavouriteContainerViewPager.adapter = makeViewPagerAdapter2(
            userFavouriteFragments
        )

        setupWithViewPager2(
            binding.userFavouriteTabLayout,
            binding.userFavouriteContainerViewPager,
            resources.getStringArray(R.array.user_favourite_tab_menu)
        )
        binding.userFavouriteContainerViewPager.offscreenPageLimit = 4

    }

}