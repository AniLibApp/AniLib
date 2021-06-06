package com.revolgenx.anilib.ui.fragment.friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
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
    private val isFollower get() = arguments?.getBoolean(IS_FOLLOWER_KEY)

    override val titleRes: Int = R.string.friend
    override val setHomeAsUp: Boolean =true


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

        binding.userFriendViewPager.adapter = makePagerAdapter(
            userFriendFragments, resources.getStringArray(
                R.array.user_friend_tab_entries
            )
        )

        binding.userFriendViewPager.currentItem = if (isFollower!!) 1 else 0
        binding.dynamicTabLayout.setupWithViewPager(binding.userFriendViewPager)

    }
}