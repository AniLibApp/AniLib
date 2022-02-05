package com.revolgenx.anilib.friend.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.adapter.makeViewPagerAdapter2
import com.revolgenx.anilib.common.ui.adapter.setupWithViewPager2
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.UserFriendContainerFragmentLayoutBinding

class UserFriendContainerFragment : BaseLayoutFragment<UserFriendContainerFragmentLayoutBinding>() {

    companion object {
        private const val USER_ID_KEY = "USER_ID_KEY"
        private const val IS_FOLLOWER_KEY = "IS_FOLLOWER_KEY"
        fun newInstance(userId: Int, isFollower: Boolean) =
            UserFriendContainerFragment().also {
                it.arguments = bundleOf(USER_ID_KEY to userId, IS_FOLLOWER_KEY to isFollower)
            }
    }

    private val userId get() = arguments?.getInt(USER_ID_KEY)
    private val isFollower get() = arguments?.getBoolean(IS_FOLLOWER_KEY) ?: false

    override val setHomeAsUp: Boolean = true

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): UserFriendContainerFragmentLayoutBinding {
        return UserFriendContainerFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    private val userFriendFragments by lazy {
        listOf(
            UserFriendFragment.newInstance(userId!!),
            UserFriendFragment.newInstance(userId!!, true)
        )
    }

    override fun getBaseToolbar(): Toolbar {
        return binding.dynamicToolbarLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userId ?: return

        binding.userFriendViewPager.adapter = makeViewPagerAdapter2(
            userFriendFragments
        )
        setupWithViewPager2(
            binding.dynamicTabLayout, binding.userFriendViewPager,
            resources.getStringArray(
                R.array.user_friend_tab_entries
            )
        )
        binding.userFriendViewPager.post {
            binding.userFriendViewPager.currentItem = if (isFollower) 1 else 0
        }

    }
}