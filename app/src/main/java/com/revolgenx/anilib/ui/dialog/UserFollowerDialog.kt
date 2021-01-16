package com.revolgenx.anilib.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.pagers.PageSizePager
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.data.meta.FollowerMeta
import com.revolgenx.anilib.data.model.user.UserFollowersModel
import com.revolgenx.anilib.databinding.FollowerDialogLayoutBinding
import com.revolgenx.anilib.databinding.UserFollowrTitleLayoutBinding
import com.revolgenx.anilib.ui.presenter.user.UserFollowerPresenter
import com.revolgenx.anilib.ui.viewmodel.user.UserFollowerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class UserFollowerDialog : BaseDialogFragment<FollowerDialogLayoutBinding>() {

    companion object {
        fun newInstance(followerMeta: FollowerMeta) = UserFollowerDialog().also {
            it.arguments = bundleOf(FOLLOWING_META_KEY to followerMeta)
        }

        const val FOLLOWING_META_KEY = "FOLLOWING_META_KEY"
    }

    private val viewModel by viewModel<UserFollowerViewModel>()
    private val loadingPresenter: Presenter<Unit> by lazy {
        Presenter.forLoadingIndicator(
            requireContext(), R.layout.loading_layout
        )
    }
    private val errorPresenter: Presenter<Unit> by lazy {
        Presenter.forErrorIndicator(requireContext(), R.layout.error_layout)
    }
    private val emptyPresenter: Presenter<Unit> by lazy {
        Presenter.forEmptyIndicator(requireContext(), R.layout.empty_layout)
    }
    private val basePresenter: Presenter<UserFollowersModel>
        get() {
            return UserFollowerPresenter(requireContext())
        }
    private val baseSource: Source<UserFollowersModel>
        get() {
            return viewModel.source ?: createSource()
        }

    private fun createSource(): Source<UserFollowersModel> {
        return viewModel.createSource()
    }

    override fun bindView(): FollowerDialogLayoutBinding {
        return FollowerDialogLayoutBinding.inflate(provideLayoutInflater())
    }

    override fun onCustomiseBuilder(
        dialogBuilder: DynamicDialog.Builder,
        savedInstanceState: Bundle?
    ): DynamicDialog.Builder {

        val followerMeta = arguments?.getParcelable<FollowerMeta>(FOLLOWING_META_KEY)
            ?: return super.onCustomiseBuilder(dialogBuilder, savedInstanceState)

        val userFollowerBinding =
            UserFollowrTitleLayoutBinding.inflate(
                LayoutInflater.from(requireContext()),
                null,
                false
            )

        userFollowerBinding.dialogTitle.text =
            if (followerMeta.isFollowing) getString(R.string.following) else getString(R.string.follower)
        userFollowerBinding.dialogDismissButton.setOnClickListener {
            dismiss()
        }
        with(dialogBuilder) {
            setCustomTitle(userFollowerBinding.root)
        }

        return super.onCustomiseBuilder(dialogBuilder, savedInstanceState)
    }

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        val followerMeta = arguments?.getParcelable<FollowerMeta>(FOLLOWING_META_KEY)
            ?: return

        viewModel.field.also {
            it.userId = followerMeta.userId
            it.isFollowing = followerMeta.isFollowing
            binding.recyclerViewFrame.setOnRefreshListener {
                createSource()
                binding.recyclerViewFrame.swipeRefreshLayout.isRefreshing = false
                createAdapter()
                    .into(binding.recyclerViewFrame.recyclerView)
            }
            createAdapter()
                .into(binding.recyclerViewFrame.recyclerView)
        }
    }

    private fun createAdapter() =
        Adapter.builder(this, 10)
            .setPager(PageSizePager(10))
            .addSource(baseSource)
            .addPresenter(basePresenter)
            .addPresenter(loadingPresenter)
            .addPresenter(errorPresenter)
            .addPresenter(emptyPresenter)

}