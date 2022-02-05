package com.revolgenx.anilib.user.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.adapter.makeViewPagerAdapter2
import com.revolgenx.anilib.common.ui.adapter.setupWithViewPager2
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.constant.UserConstant
import com.revolgenx.anilib.user.data.meta.UserMeta
import com.revolgenx.anilib.databinding.UserFavouriteContainerFragmentLayoutBinding
import com.revolgenx.anilib.user.viewmodel.UserProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFavouriteContainerFragment :
    BaseLayoutFragment<UserFavouriteContainerFragmentLayoutBinding>() {


    companion object{
        fun newInstance(userMeta: UserMeta) = UserFavouriteContainerFragment().also {
            it.arguments = bundleOf(UserConstant.USER_META_KEY to userMeta)
        }
    }

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

    private val userMeta get()= arguments?.getParcelable<UserMeta?>(UserConstant.USER_META_KEY)


    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): UserFavouriteContainerFragmentLayoutBinding {
        return UserFavouriteContainerFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val user = userMeta?: return

        with(userProfileViewModel.userField) {
            userName = user.userName
            userId = user.userId
        }

        userFavouriteFragments.forEach {
            it.userProfileViewModel = this.userProfileViewModel
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