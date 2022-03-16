package com.revolgenx.anilib.friend.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.friend.presenter.UserFriendPresenter
import com.revolgenx.anilib.friend.viewmodel.FriendViewModel
import com.revolgenx.anilib.user.data.model.UserModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFriendFragment : BasePresenterFragment<UserModel>() {
    override val basePresenter: Presenter<UserModel>
        get() = UserFriendPresenter(requireContext())
    override val baseSource: Source<UserModel>
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

    override fun createSource(): Source<UserModel> {
        return viewModel.createSource()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            field.userId = it.getInt(USER_ID_KEY)
            field.isFollower = it.getBoolean(IS_FOLLOWER_KEY)
        } ?: return

    }
}