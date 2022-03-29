package com.revolgenx.anilib.social.ui.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.R
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.databinding.ListActivityPresenterLayoutBinding
import com.revolgenx.anilib.databinding.MessageActivityPresenterLayoutBinding
import com.revolgenx.anilib.databinding.TextActivityPresenterLayoutBinding
import com.revolgenx.anilib.common.event.*
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.data.model.ListActivityModel
import com.revolgenx.anilib.social.data.model.MessageActivityModel
import com.revolgenx.anilib.social.data.model.TextActivityModel
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.social.ui.viewmodel.ActivityInfoViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityMessageComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityTextComposerViewModel
import com.revolgenx.anilib.type.ActivityType
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.ui.view.makeArrayPopupMenu
import com.revolgenx.anilib.ui.view.makeConfirmationDialog
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.copyToClipBoard
import com.revolgenx.anilib.util.openLink

class ActivityUnionPresenter(
    context: Context,
    private val viewModel: ActivityUnionViewModel,
    private val activityInfoViewModel: ActivityInfoViewModel,
    private val textComposerViewModel: ActivityTextComposerViewModel,
    private val messageComposerViewModel: ActivityMessageComposerViewModel
) :
    BasePresenter<ViewBinding, ActivityUnionModel>(context) {

    companion object {
        private const val ACTIVITY_UNION_HOLDER_ITEM_KEY = "ACTIVITY_UNION_HOLDER_ITEM_KEY"
    }

    override val elementTypes: Collection<Int> =
        listOf(
            ActivityType.TEXT.ordinal,
            ActivityType.MEDIA_LIST.ordinal,
            ActivityType.ANIME_LIST.ordinal,
            ActivityType.MANGA_LIST.ordinal,
            ActivityType.MESSAGE.ordinal
        )

    private val activityMenuEntries by lazy {
        context.resources.getStringArray(R.array.activity_more_entries)
    }

    private val userId = UserPreference.userId

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): ViewBinding {
        return when (elementType) {
            ActivityType.MEDIA_LIST.ordinal,
            ActivityType.ANIME_LIST.ordinal,
            ActivityType.MANGA_LIST.ordinal -> {
                ListActivityPresenterLayoutBinding.inflate(inflater, parent, false)
            }
            ActivityType.MESSAGE.ordinal -> {
                MessageActivityPresenterLayoutBinding.inflate(inflater, parent, false)
            }
            else -> {
                TextActivityPresenterLayoutBinding.inflate(inflater, parent, false)
            }
        }
    }


    override fun onBind(page: Page, holder: Holder, element: Element<ActivityUnionModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        val binding = holder.getBinding()?: return
        when (binding) {
            is TextActivityPresenterLayoutBinding -> {
                binding.bind(item as TextActivityModel)
            }
            is MessageActivityPresenterLayoutBinding -> {
                binding.bind(item as MessageActivityModel)
            }
            is ListActivityPresenterLayoutBinding->{
                binding.bind(item as ListActivityModel)
            }
            else -> {
                return
            }
        }


        holder[ACTIVITY_UNION_HOLDER_ITEM_KEY] = item
        item.onDataChanged = {
            when (binding) {
                is TextActivityPresenterLayoutBinding -> {
                    binding.updateItems(item)
                }
                is MessageActivityPresenterLayoutBinding->{
                    binding.updateItems(item)
                }
                is ListActivityPresenterLayoutBinding -> {
                    binding.updateItems(item)
                }
            }
        }
    }

    override fun onUnbind(holder: Holder) {
        super.onUnbind(holder)
        holder.get<ActivityUnionModel?>(ACTIVITY_UNION_HOLDER_ITEM_KEY)?.onDataChanged = null
    }

    private fun TextActivityPresenterLayoutBinding.bind(item: TextActivityModel) {
        AlMarkwonFactory.getMarkwon().setParsedMarkdown(textActivityTv, item.textSpanned)

        userNameTv.text = item.user!!.name
        activityReplyCountTv.text = item.replyCount.toString()

        updateItems(item)


        userAvatarIv.setImageURI(item.user!!.avatar?.large)
        activityCreatedAtTv.text = item.createdAt

        userAvatarIv.setOnClickListener {
            openUser(item.userId)
        }
        userNameTv.setOnClickListener {
            openUser(item.userId)
        }

        activityLikeContainer.setOnClickListener {
            viewModel.toggleActivityLike(item) {
                updateItems(item)
            }
        }


        activitySubscribeIv.setOnClickListener {
            viewModel.toggleActivitySubscription(item) {
                updateItems(item)
            }
        }

        activityReplyContainer.setOnClickListener {
            activityInfoViewModel.activeModel = item
            OpenActivityInfoEvent(item.id).postEvent
        }

        if (BuildConfig.DEBUG) {
            activityMorePopup.setOnLongClickListener {
                textComposerViewModel.activeModel = item
                OpenActivityTextComposer().postEvent
                true
            }
        }

        activityMorePopup.setOnClickListener {
            val filteredMenu =
                activityMenuEntries.filterIndexed { index, _ -> if (item.userId == userId) true else index < 2 }
                    .toTypedArray()
            makeArrayPopupMenu(it, filteredMenu) { _, _, position, _ ->
                when (position) {
                    0 -> {
                        context.openLink(item.siteUrl)
                    }
                    1 -> {
                        context.copyToClipBoard(item.siteUrl)
                    }
                    2 -> {
                        textComposerViewModel.activeModel = item
                        OpenActivityTextComposer().postEvent
                    }
                    3 -> {
                        makeConfirmationDialog(context) {
                            context.makeToast(R.string.please_wait)
                            viewModel.deleteActivity(item.id) { id, success ->
                                if (id == item.id) {
                                    if (success) {
                                        context.makeToast(R.string.deleted_successfully_please_refresh)
                                    } else {
                                        context.makeToast(R.string.failed_to_delete)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        root.setOnClickListener {
            activityInfoViewModel.activeModel = item
            OpenActivityInfoEvent(item.id).postEvent
        }

    }

    private fun MessageActivityPresenterLayoutBinding.bind(item: MessageActivityModel) {
        AlMarkwonFactory.getMarkwon().setParsedMarkdown(textActivityTv, item.messageSpanned)

        userNameTv.text = item.messenger!!.name
        activityReplyCountTv.text = item.replyCount.toString()

        updateItems(item)

        userAvatarIv.setImageURI(item.messenger!!.avatar?.large)
        activityCreatedAtTv.text = item.createdAt

        userAvatarIv.setOnClickListener {
            openUser(item.messengerId)
        }
        userNameTv.setOnClickListener {
            openUser(item.messengerId)
        }

        activityLikeContainer.setOnClickListener {
            viewModel.toggleActivityLike(item) {
                updateItems(item)
            }
        }


        activitySubscribeIv.setOnClickListener {
            viewModel.toggleActivitySubscription(item) {
                updateItems(item)
            }
        }

        activityReplyContainer.setOnClickListener {
            activityInfoViewModel.activeModel = item
            OpenActivityInfoEvent(item.id).postEvent
        }

        if (BuildConfig.DEBUG) {
            activityMorePopup.setOnLongClickListener {
                messageComposerViewModel.activeModel = item
                OpenActivityMessageComposer(viewModel.field.userId!!).postEvent
                true
            }
        }

        activityMorePopup.setOnClickListener {
            val filteredMenu =
                activityMenuEntries.filterIndexed { index, _ -> if (item.userId == userId) true else index < 2 }
                    .toTypedArray()
            makeArrayPopupMenu(it, filteredMenu) { _, _, position, _ ->
                when (position) {
                    0 -> {
                        context.openLink(item.siteUrl)
                    }
                    1 -> {
                        context.copyToClipBoard(item.siteUrl)
                    }
                    2 -> {
                        messageComposerViewModel.activeModel = item
                        OpenActivityMessageComposer(viewModel.field.userId!!).postEvent
                    }
                    3 -> {
                        makeConfirmationDialog(context) {
                            context.makeToast(R.string.please_wait)
                            viewModel.deleteActivity(item.id) { id, success ->
                                if (id == item.id) {
                                    if (success) {
                                        context.makeToast(R.string.deleted_successfully_please_refresh)
                                    } else {
                                        context.makeToast(R.string.failed_to_delete)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        root.setOnClickListener {
            activityInfoViewModel.activeModel = item
            OpenActivityInfoEvent(item.id).postEvent
        }

    }

    private fun TextActivityPresenterLayoutBinding.updateItems(item: ActivityUnionModel) {
        activityLikeCountTv.text = item.likeCount.toString()
        activityReplyCountTv.text = item.replyCount.toString()
        activitySubscribeIv.setImageResource(if (item.isSubscribed) R.drawable.ic_notification_filled else R.drawable.ic_notification_outline)
        activityLikeIv.setImageResource(if (item.isLiked) R.drawable.ic_activity_like_filled else R.drawable.ic_activity_like_outline)
    }

    private fun MessageActivityPresenterLayoutBinding.updateItems(item: ActivityUnionModel) {
        activityLikeCountTv.text = item.likeCount.toString()
        activityReplyCountTv.text = item.replyCount.toString()
        activitySubscribeIv.setImageResource(if (item.isSubscribed) R.drawable.ic_notification_filled else R.drawable.ic_notification_outline)
        activityLikeIv.setImageResource(if (item.isLiked) R.drawable.ic_activity_like_filled else R.drawable.ic_activity_like_outline)
    }

    private fun ListActivityPresenterLayoutBinding.updateItems(item: ActivityUnionModel) {
        activityLikeCountTv.text = item.likeCount.toString()
        activityReplyCountTv.text = item.replyCount.toString()
        activitySubscribeIv.setImageResource(if (item.isSubscribed) R.drawable.ic_notification_filled else R.drawable.ic_notification_outline)
        activityLikeIv.setImageResource(if (item.isLiked) R.drawable.ic_activity_like_filled else R.drawable.ic_activity_like_outline)
    }

    private fun ListActivityPresenterLayoutBinding.bind(item: ListActivityModel) {
        userNameTv.text = item.user!!.name

        activityListStatus.text = item.getProgressStatus
        activityReplyCountTv.text = item.replyCount.toString()
        updateItems(item)

        userAvatarIv.setImageURI(item.user!!.avatar?.large)
        mediaCoverIv.setImageURI(item.media!!.coverImage?.image())

        activityCreatedAtTv.text = item.createdAt

        //update like
        activityLikeContainer.setOnClickListener {
            viewModel.toggleActivityLike(item) {
                updateItems(item)
            }
        }

        activitySubscribeIv.setOnClickListener {
            viewModel.toggleActivitySubscription(item) {
                updateItems(item)
            }
        }

        userAvatarIv.setOnClickListener {
            openUser(item.userId)
        }
        userNameTv.setOnClickListener {
            openUser(item.userId)
        }
        mediaCoverIv.setOnClickListener {
            openMedia(item.media!!)
        }
        activityListStatus.setOnClickListener {
            openMedia(item.media!!)
        }
        activityMorePopup.setOnClickListener {
            val filteredMenu =
                activityMenuEntries.filterIndexed { index, _ -> if (item.userId == userId) index != 2 else index < 2 }
                    .toTypedArray()
            makeArrayPopupMenu(it, filteredMenu) { _, _, position, _ ->
                when (position) {
                    0 -> {
                        context.openLink(item.siteUrl)
                    }
                    1 -> {
                        context.copyToClipBoard(item.siteUrl)
                    }
                    2 -> {
                        makeConfirmationDialog(context) {
                            context.makeToast(R.string.please_wait)
                            viewModel.deleteActivity(item.id) { id, success ->
                                if (id == item.id) {
                                    if (success) {
                                        context.makeToast(R.string.deleted_successfully_please_refresh)
                                    } else {
                                        context.makeToast(R.string.failed_to_delete)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        root.setOnClickListener {
            activityInfoViewModel.activeModel = item
            OpenActivityInfoEvent(item.id).postEvent
        }
    }

    private fun openUser(userId: Int?) {
        OpenUserProfileEvent(userId).postEvent
    }

    private fun openMedia(item: MediaModel) {
        OpenMediaInfoEvent(
            MediaInfoMeta(
                item.id,
                item.type!!,
                item.title!!.userPreferred,
                item.coverImage!!.image(),
                item.coverImage!!.largeImage,
                item.bannerImage
            )
        ).postEvent
    }
}