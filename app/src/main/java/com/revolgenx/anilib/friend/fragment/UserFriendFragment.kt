package com.revolgenx.anilib.friend.fragment

import android.os.Bundle
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.friend.data.model.FriendModel
import com.revolgenx.anilib.friend.presenter.UserFriendPresenter
import com.revolgenx.anilib.friend.viewmodel.FriendViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFriendFragment : BasePresenterFragment<FriendModel>() {
    override val basePresenter: Presenter<FriendModel>
        get() = UserFriendPresenter(requireContext())
    override val baseSource: Source<FriendModel>
        get() = viewModel.source ?: createSource()


    private val viewModel: FriendViewModel by viewModel()
    private val field get() = viewModel.field

    companion object {
        private const val IS_FOLLOWER_KEY = "IS_FOLLOWER_KEY"
        private const val USER_ID_KEY = "USER_ID_KEY"
        fun newInstance(userId: Int, isFollower: Boolean = false) = UserFriendFragment().also {
            it.arguments = bundleOf(IS_FOLLOWER_KEY to isFollower, USER_ID_KEY to userId)
        }
    }

    override fun createSource(): Source<FriendModel> {
        return viewModel.createSource()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            field.userId = it.getInt(USER_ID_KEY)
            field.isFollower = it.getBoolean(IS_FOLLOWER_KEY)
        } ?: return

    }
}