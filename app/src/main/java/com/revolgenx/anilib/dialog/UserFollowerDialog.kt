package com.revolgenx.anilib.dialog

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.pagers.PageSizePager
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.revolgenx.anilib.R
import com.revolgenx.anilib.meta.FollowerMeta
import com.revolgenx.anilib.model.user.UserFollowersModel
import com.revolgenx.anilib.presenter.user.UserFollowerPresenter
import com.revolgenx.anilib.viewmodel.user.UserFollowerViewModel
import kotlinx.android.synthetic.main.follower_dialog_layout.*
import kotlinx.android.synthetic.main.user_followr_title_layout.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class UserFollowerDialog : DynamicDialogFragment() {

    companion object {
        fun newInstance(followerMeta: FollowerMeta) = UserFollowerDialog().also {
            it.arguments = bundleOf(FOLLOWING_META_KEY to followerMeta)
        }

        const val FOLLOWING_META_KEY = "FOLLOWING_META_KEY"
    }

    private val viewModel by viewModel<UserFollowerViewModel>()

    private val loadingPresenter: Presenter<Void> by lazy {
        Presenter.forLoadingIndicator(
            requireContext(), R.layout.loading_layout
        )
    }

    private val errorPresenter: Presenter<Void> by lazy {
        Presenter.forErrorIndicator(requireContext(), R.layout.error_layout)
    }

    private val emptyPresenter: Presenter<Void> by lazy {
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

    private var baseRecyclerView: RecyclerView? = null

    override fun onCustomiseBuilder(
        dialogBuilder: DynamicDialog.Builder,
        savedInstanceState: Bundle?
    ): DynamicDialog.Builder {

        val followerMeta = arguments?.getParcelable<FollowerMeta>(FOLLOWING_META_KEY)
            ?: return super.onCustomiseBuilder(dialogBuilder, savedInstanceState)

        val titleView =
            LayoutInflater.from(requireContext()).inflate(R.layout.user_followr_title_layout, null)
        titleView.dialogTitle.text =
            if (followerMeta.isFollowing) getString(R.string.following) else getString(R.string.follower)
        titleView.dialogDismissButton.setOnClickListener {
            dismiss()
        }
        with(dialogBuilder) {
            setCustomTitle(titleView)
            setView(R.layout.follower_dialog_layout)
        }

        return super.onCustomiseBuilder(dialogBuilder, savedInstanceState)
    }

    override fun onCustomiseDialog(
        alertDialog: DynamicDialog,
        savedInstanceState: Bundle?
    ): DynamicDialog {
        val followerMeta = arguments?.getParcelable<FollowerMeta>(FOLLOWING_META_KEY)
            ?: return super.onCustomiseDialog(alertDialog, savedInstanceState)

        alertDialog.apply {
            setOnShowListener {
                viewModel.field.also {
                    it.userId = followerMeta.userId
                    it.isFollowing = followerMeta.isFollowing
                }
                with(recyclerViewFrame) {
                    baseRecyclerView = recyclerView
                    setOnRefreshListener {
                        createSource()
                        swipeRefreshLayout.isRefreshing = false
                        invalidateAdapter()
                    }
                    invalidateAdapter()
                }

            }
        }
        return super.onCustomiseDialog(alertDialog, savedInstanceState)
    }

    private fun invalidateAdapter() {
        Adapter.builder(this, 10)
            .setPager(PageSizePager(10))
            .addSource(baseSource)
            .addPresenter(basePresenter)
            .addPresenter(loadingPresenter)
            .addPresenter(errorPresenter)
            .addPresenter(emptyPresenter)
            .into(baseRecyclerView!!)
    }

}