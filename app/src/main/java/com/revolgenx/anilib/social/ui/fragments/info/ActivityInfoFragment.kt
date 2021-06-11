package com.revolgenx.anilib.social.ui.fragments.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.data.meta.MediaInfoMeta
import com.revolgenx.anilib.databinding.ActivityInfoFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenMediaInfoEvent
import com.revolgenx.anilib.infrastructure.event.OpenUserProfileEvent
import com.revolgenx.anilib.infrastructure.repository.util.Status.*
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.data.model.ListActivityModel
import com.revolgenx.anilib.social.data.model.TextActivityModel
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.social.ui.presenter.ActivityLikeUserPresenter
import com.revolgenx.anilib.social.ui.presenter.ActivityReplyPresenter
import com.revolgenx.anilib.social.ui.viewmodel.ActivityInfoViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.ui.view.makeToast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityInfoFragment : BaseLayoutFragment<ActivityInfoFragmentLayoutBinding>() {

    companion object {
        private const val ACTIVITY_ID_KEY = "ACTIVITY_ID_KEY"
        fun newInstance(activityId: Int) = ActivityInfoFragment().also {
            it.arguments = bundleOf(ACTIVITY_ID_KEY to activityId)
        }
    }

    override val setHomeAsUp: Boolean = true
    override val menuRes: Int = R.menu.activity_info_menu

    //like users
    private val likeUserPresenter get() = ActivityLikeUserPresenter(requireContext())
    private val likeUserLayoutManager
        get() = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL,
            false
        )

    //replies
    private val repliesPresenter get() = ActivityReplyPresenter(requireContext())
    private val repliesLayoutManager get() = LinearLayoutManager(requireContext())

    private val activityId get() = arguments?.getInt(ACTIVITY_ID_KEY)

    private val viewModel by viewModel<ActivityInfoViewModel>()
    private val activityUnionViewModel by sharedViewModel<ActivityUnionViewModel>()

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ActivityInfoFragmentLayoutBinding {
        return ActivityInfoFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun getBaseToolbar(): Toolbar {
        return binding.activityInfoToolbar.dynamicToolbar
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.activity_reply_menu -> {

                true
            }
            else -> {
                false
            }
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.field.activityId =
            activityId ?: activityUnionViewModel.activeActivityUnionModel?.id ?: return

        binding.initListener()

        viewModel.activityInfoLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    val item = it.data ?: return@observe
                    updateViews(item)
                    showLoading(false)
                    showErrorLayout(false)
                }
                ERROR -> {
                    showLoading(false)
                    showErrorLayout(true)
                    binding.errorLayout.errorMsg.text = it.message
                }
                LOADING -> {
                    it.data?.let {
                        updateViews(it)
                    }
                    showLoading(true)
                    showErrorLayout(false)
                }
            }
        }

        if (savedInstanceState == null) {
            activityUnionViewModel.activeActivityUnionModel?.let {
                viewModel.setActivityInfo(it)
            }

            viewModel.getActivityInfo()
        }
    }

    private fun showErrorLayout(show: Boolean) {
        binding.errorContainerLayout.visibility = if (show) View.VISIBLE else {
            binding.errorLayout.errorMsg.text = ""
            View.GONE
        }
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.activityInfoSwipeToRefresh.post {
                binding.activityInfoSwipeToRefresh.isRefreshing = true
            }
        } else {
            binding.activityInfoSwipeToRefresh.isRefreshing = false
        }
    }

    private fun ActivityInfoFragmentLayoutBinding.initListener() {
        activityInfoSwipeToRefresh.setOnRefreshListener {
            viewModel.getActivityInfo()
        }

        listActivityLikeContainer.setOnClickListener {
            viewModel.getActivityUnionModel()?.let {
                activityUnionViewModel.toggleActivityLike(it) { re ->
                    updateItems(it)
                    when (re.status) {
                        SUCCESS -> {
                            val item = re.data ?: return@toggleActivityLike
                            activityUnionViewModel.activeActivityUnionModel?.let {
                                it.isLiked = item.isLiked
                                it.likeCount = item.likeCount
                                it.onDataChanged?.invoke()
                            }
                        }
                        ERROR -> {
                            makeToast(R.string.failed_to_toggle)
                        }
                    }
                }
            }
        }


        activitySubscribeIv.setOnClickListener {
            updateSubscription()
        }
    }

    private fun ActivityInfoFragmentLayoutBinding.updateSubscription(){
        viewModel.getActivityUnionModel()?.let {
            activityUnionViewModel.toggleActivitySubscription(it){re->
                updateItems(it)
                when(re.status){
                    SUCCESS->{
                        val item = re.data?: return@toggleActivitySubscription
                        activityUnionViewModel.activeActivityUnionModel?.let {
                            it.isSubscribed = item.isSubscribed
                            it.onDataChanged?.invoke()
                        }
                    }
                    ERROR->{
                        makeToast(R.string.failed_to_toggle)
                    }
                }
            }
        }
    }

    private fun updateViews(item: ActivityUnionModel) {
        when (item) {
            is TextActivityModel -> {
                binding.textActivityContainerLayout.visibility = View.VISIBLE
                binding.bindTextActivity(item)

            }
            is ListActivityModel -> {
                binding.listActivityContainerLayout.visibility = View.VISIBLE
                binding.bindListActivity(item)
            }
        }

        binding.apply {
            repliesCountTv.text = getString(R.string.replies_count).format(item.replyCount)

            updateItems(item)

            if (likeUserRecyclerView.layoutManager == null) {
                likeUserRecyclerView.layoutManager = likeUserLayoutManager
            }

            if (activityRepliesRecyclerView.layoutManager == null) {
                activityRepliesRecyclerView.layoutManager = repliesLayoutManager
            }

            likeUserRecyclerView.adapter = Adapter.builder(viewLifecycleOwner)
                .addPresenter(likeUserPresenter)
                .addSource(Source.fromList(item.likes ?: emptyList()))
                .build()

            activityRepliesRecyclerView.adapter = Adapter.builder(viewLifecycleOwner)
                .addPresenter(repliesPresenter)
                .addSource(Source.fromList(item.replies ?: emptyList()))
                .build()

        }

    }

    private fun ActivityInfoFragmentLayoutBinding.updateItems(item: ActivityUnionModel) {
        activityLikeCountTv.text = item.likeCount.toString()
        listActivityLikeIv.setImageResource(if(item.isLiked) R.drawable.ic_activity_like_filled else R.drawable.ic_activity_like_outline)

        when(item){
            is TextActivityModel->{
                activitySubscribeIv.setImageResource(if(item.isSubscribed) R.drawable.ic_notification_filled else R.drawable.ic_notification_outline)
            }
            is ListActivityModel->{
                listActivitySubscribeIv.setImageResource(if(item.isSubscribed) R.drawable.ic_notification_filled else R.drawable.ic_notification_outline)
            }
        }
    }

    private fun ActivityInfoFragmentLayoutBinding.bindTextActivity(item: TextActivityModel) {
        userAvatarIv.setImageURI(item.user?.avatar?.image)
        userNameTv.text = item.user?.name
        activityCreatedAtTv.text = item.createdAt
        if (textActivityTv.text.isBlank()) {
            AlMarkwonFactory.getMarkwon().setParsedMarkdown(textActivityTv, item.textSpanned)
        }
    }

    private fun ActivityInfoFragmentLayoutBinding.bindListActivity(item: ListActivityModel) {
        listUserAvatarIv.setImageURI(item.user?.avatar?.image)
        listUserNameTv.text = item.user?.name
        listActivityCreatedAtTv.text = item.createdAt
        item.media?.let { media ->
            mediaCoverIv.setImageURI(media.coverImage?.image(requireContext()))
            mediaCoverIv.setOnClickListener {
                OpenMediaInfoEvent(
                    MediaInfoMeta(
                        media.id,
                        media.type!!,
                        media.title!!.userPreferred,
                        media.coverImage!!.image(requireContext()),
                        media.coverImage!!.largeImage,
                        media.bannerImage
                    )
                ).postEvent
            }
        }

        activityListStatus.text = item.getProgressStatus

        listUserAvatarIv.setOnClickListener {
            openUserProfile(item.user?.id)
        }
        listUserNameTv.setOnClickListener {
            openUserProfile(item.user?.id)
        }
    }

    private fun openUserProfile(userId: Int?) {
        OpenUserProfileEvent(userId).postEvent
    }

    override fun onDestroyView() {
        activityUnionViewModel.activeActivityUnionModel = null
        super.onDestroyView()
    }
}