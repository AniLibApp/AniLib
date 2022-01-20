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
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.databinding.ActivityInfoFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.event.*
import com.revolgenx.anilib.infrastructure.repository.util.Status.*
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.data.model.ListActivityModel
import com.revolgenx.anilib.social.data.model.MessageActivityModel
import com.revolgenx.anilib.social.data.model.TextActivityModel
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.social.ui.presenter.ActivityLikeUserPresenter
import com.revolgenx.anilib.social.ui.presenter.ActivityReplyPresenter
import com.revolgenx.anilib.social.ui.viewmodel.ActivityInfoViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityReplyViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityReplyComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityTextComposerViewModel
import com.revolgenx.anilib.ui.view.makeArrayPopupMenu
import com.revolgenx.anilib.ui.view.makeConfirmationDialog
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.*
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityInfoFragment : BaseLayoutFragment<ActivityInfoFragmentLayoutBinding>(),
    EventBusListener {

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
    private val repliesPresenter
        get() = ActivityReplyPresenter(
            requireContext(),
            activityReplyViewModel,
            activityReplyComposerViewModel
        )
    private val repliesLayoutManager get() = LinearLayoutManager(requireContext())

    private val activityId get() = arguments?.getInt(ACTIVITY_ID_KEY)

    private val viewModel by sharedViewModel<ActivityInfoViewModel>()
    private val activityUnionViewModel by viewModel<ActivityUnionViewModel>()
    private val activityReplyComposerViewModel by sharedViewModel<ActivityReplyComposerViewModel>()
    private val textComposerViewModel by sharedViewModel<ActivityTextComposerViewModel>()

    private val activityReplyViewModel by viewModel<ActivityReplyViewModel>()

    private val activityMenuEntries by lazy {
        requireContext().resources.getStringArray(R.array.activity_more_entries)
    }

    private val userId by lazy {
        requireContext().userId()
    }


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
                viewModel.field.activityId?.let {
                    OpenActivityReplyComposer(it).postEvent
                }
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.field.activityId =
            activityId ?: viewModel.activeModel?.id ?: return

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
                    showLoading(true)
                    showErrorLayout(false)
                }
            }
        }

        if (savedInstanceState == null) {
            viewModel.activeModel?.let {
                viewModel.setActivityInfo(it)
                updateViews(it)
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
                            viewModel.activeModel?.let {
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

        listActivitySubscribeIv.setOnClickListener {
            updateSubscription()
        }

        listActivityMorePopup.setOnClickListener {

        }
    }

    private fun ActivityInfoFragmentLayoutBinding.updateSubscription() {
        viewModel.getActivityUnionModel()?.let {
            activityUnionViewModel.toggleActivitySubscription(it) { re ->
                updateItems(it)
                when (re.status) {
                    SUCCESS -> {
                        val item = re.data ?: return@toggleActivitySubscription
                        viewModel.activeModel?.let {
                            it.isSubscribed = item.isSubscribed
                            it.onDataChanged?.invoke()
                        }
                    }
                    ERROR -> {
                        makeToast(R.string.failed_to_toggle)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun updateViews(item: ActivityUnionModel) {
        when (item) {
            is TextActivityModel, is MessageActivityModel -> {
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
        listActivityLikeIv.setImageResource(if (item.isLiked) R.drawable.ic_activity_like_filled else R.drawable.ic_activity_like_outline)

        when (item) {
            is TextActivityModel, is MessageActivityModel -> {
                activitySubscribeIv.setImageResource(if (item.isSubscribed) R.drawable.ic_notification_filled else R.drawable.ic_notification_outline)
            }
            is ListActivityModel -> {
                listActivitySubscribeIv.setImageResource(if (item.isSubscribed) R.drawable.ic_notification_filled else R.drawable.ic_notification_outline)
            }
        }
    }

    private fun ActivityInfoFragmentLayoutBinding.bindTextActivity(item: ActivityUnionModel) {
        userAvatarIv.setImageURI(item.user?.avatar?.image)
        userNameTv.text = item.user?.name
        activityCreatedAtTv.text = item.createdAt

        when (item) {
            is TextActivityModel -> {
                if (textActivityTv.text.isBlank() || viewModel.activeModel == null || (viewModel.activeModel as? TextActivityModel?)?.text != item.text) {
                    AlMarkwonFactory.getMarkwon()
                        .setParsedMarkdown(textActivityTv, item.textSpanned)
                }
            }
            is MessageActivityModel -> {
                if (textActivityTv.text.isBlank() || viewModel.activeModel == null || (viewModel.activeModel as? MessageActivityModel?)?.message != item.message) {
                    AlMarkwonFactory.getMarkwon()
                        .setParsedMarkdown(textActivityTv, item.messageSpanned)
                }
            }
        }

        userAvatarIv.setOnClickListener {
            openUserProfile(item.user?.id)
        }
        userNameTv.setOnClickListener {
            openUserProfile(item.user?.id)
        }

        activityMorePopup.setOnClickListener {
            val filteredMenu =
                activityMenuEntries.filterIndexed { index, _ -> if (item.userId == userId) true else index < 2 }
                    .toTypedArray()
            makeArrayPopupMenu(it, filteredMenu) { _, _, position, _ ->
                when (position) {
                    0 -> {
                        requireContext().openLink(item.siteUrl)
                    }
                    1 -> {
                        requireContext().copyToClipBoard(item.siteUrl)
                    }
                    2 -> {
                        textComposerViewModel.activeModel = item
                        OpenActivityTextComposer().postEvent
                    }
                    3 -> {
                        makeConfirmationDialog(requireContext()) {
                            requireContext().makeToast(R.string.please_wait)
                            activityUnionViewModel.deleteActivity(item.id) { id, success ->
                                if (id == item.id) {
                                    if (success) {
                                        requireActivity().makeToast(R.string.deleted_successfully_please_refresh)
                                    } else {
                                        requireActivity().makeToast(R.string.failed_to_delete)
                                    }
                                    popBackStack()
                                }
                            }
                        }
                    }
                }
            }
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

        listActivityMorePopup.setOnClickListener {
            val filteredMenu =
                activityMenuEntries.filterIndexed { index, _ -> if (item.userId == userId) index != 2 else index < 2 }
                    .toTypedArray()
            makeArrayPopupMenu(it, filteredMenu) { _, _, position, _ ->
                when (position) {
                    0 -> {
                        requireContext().openLink(item.siteUrl)
                    }
                    1 -> {
                        requireContext().copyToClipBoard(item.siteUrl)
                    }
                    3 -> {
                        makeConfirmationDialog(requireContext()) {
                            requireContext().makeToast(R.string.please_wait)
                            activityUnionViewModel.deleteActivity(item.id) { id, success ->
                                if (id == item.id) {
                                    if (success) {
                                        requireActivity().makeToast(R.string.deleted_successfully_please_refresh)
                                    } else {
                                        requireActivity().makeToast(R.string.failed_to_delete)
                                    }
                                    popBackStack()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun openUserProfile(userId: Int?) {
        OpenUserProfileEvent(userId).postEvent
    }

    override fun onDestroyView() {
        viewModel.activeModel = null
        super.onDestroyView()
    }

    //Events
    @Subscribe
    fun onEvent(event: OnActivityInfoUpdateEvent) {
        if (event.activityId == activityId) {
            viewModel.getActivityInfo()
        }
    }
}