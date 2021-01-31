package com.revolgenx.anilib.ui.fragment.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.constant.UserConstant
import com.revolgenx.anilib.data.meta.UserMeta
import com.revolgenx.anilib.databinding.UserFavouriteContainerFragmentLayoutBinding
import com.revolgenx.anilib.ui.viewmodel.user.UserProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFavouriteContainerFragment :
    BaseLayoutFragment<UserFavouriteContainerFragmentLayoutBinding>() {

    private val userFavouriteFragments by lazy {
        listOf(
            AnimeFavouriteFragment(),
            MangaFavouriteFragment(),
            CharacterFavouriteFragment(),
            StaffFavouriteFragment(),
            StudioFavouriteFragment()
        )
    }

    private val userProfileViewModel: UserProfileViewModel by viewModel()

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): UserFavouriteContainerFragmentLayoutBinding {
        return UserFavouriteContainerFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val userMeta: UserMeta = arguments?.getParcelable(UserConstant.USER_META_KEY) ?: return

        with(userProfileViewModel.userField) {
            userName = userMeta.userName
            userId = userMeta.userId
        }

        userFavouriteFragments.forEach {
            it.userProfileViewModel = this.userProfileViewModel
        }

        val adapter = makePagerAdapter(
            userFavouriteFragments,
            resources.getStringArray(R.array.user_favourite_tab_menu)
        )
        binding.userFavouriteContainerViewPager.adapter = adapter
        binding.userFavouriteContainerViewPager.offscreenPageLimit = 4
        binding.userFavouriteTabLayout.setupWithViewPager(binding.userFavouriteContainerViewPager)

    }

}