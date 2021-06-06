package com.revolgenx.anilib.social.ui.presenter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.MarkwonPlaygroundActivity
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.data.meta.MediaInfoMeta
import com.revolgenx.anilib.data.model.CommonMediaModel
import com.revolgenx.anilib.databinding.ListActivityPresenterLayoutBinding
import com.revolgenx.anilib.databinding.TextActivityPresenterLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenActivityComposer
import com.revolgenx.anilib.infrastructure.event.OpenActivityInfoEvent
import com.revolgenx.anilib.infrastructure.event.OpenMediaInfoEvent
import com.revolgenx.anilib.infrastructure.event.OpenUserProfileEvent
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.data.model.ListActivityModel
import com.revolgenx.anilib.social.data.model.TextActivityModel
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.type.ActivityType
import com.revolgenx.anilib.ui.dialog.MessageDialog
import com.revolgenx.anilib.ui.presenter.BasePresenter
import com.revolgenx.anilib.ui.view.makeArrayPopupMenu
import com.revolgenx.anilib.ui.view.makeConfirmationDialog
import com.revolgenx.anilib.ui.view.makeDynamicToastView
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.copyToClipBoard
import com.revolgenx.anilib.util.openLink

class ActivityUnionPresenter(
    context: Context,
    private val viewModel: ActivityUnionViewModel
) :
    BasePresenter<ViewBinding, ActivityUnionModel>(context) {
    override val elementTypes: Collection<Int> =
        listOf(
            ActivityType.TEXT.ordinal,
            ActivityType.MEDIA_LIST.ordinal,
            ActivityType.ANIME_LIST.ordinal,
            ActivityType.MANGA_LIST.ordinal
        )

    private val activityMenuEntries by lazy {
        context.resources.getStringArray(R.array.activity_more_entries)
    }

    private val userId by lazy {
        context.userId()
    }

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
            else -> {
                TextActivityPresenterLayoutBinding.inflate(inflater, parent, false)
            }
        }
    }

    override fun onBind(page: Page, holder: Holder, element: Element<ActivityUnionModel>) {
        super.onBind(page, holder, element)

        val item = element.data ?: return
        val type = element.type

        if (type == ActivityType.TEXT.ordinal) {
            (holder.getBinding() as TextActivityPresenterLayoutBinding).bind(item as TextActivityModel)
        } else {
            (holder.getBinding() as ListActivityPresenterLayoutBinding).bind(item as ListActivityModel)
        }
    }

    private fun TextActivityPresenterLayoutBinding.bind(item: TextActivityModel) {
        AlMarkwonFactory.getMarkwon().setParsedMarkdown(textActivityTv, item.textSpanned)

        root.setOnClickListener {
//            context.startActivity(Intent(context, MarkwonPlaygroundActivity::class.java).also {
//                it.putExtra(MarkwonPlaygroundActivity.SPANNED_DATA_KEY, item.text)
//            })
            viewModel.activeActivityUnionModel = item
            OpenActivityInfoEvent(item.id!!).postEvent
        }

        userNameTv.text = item.user!!.name

        updateLike(item)
        activityReplyCountTv.text = item.replyCount.toString()

        userAvatarIv.setImageURI(item.user!!.avatar?.large)

        activityCreatedAtTv.text = item.createdAt

        userAvatarIv.setOnClickListener {
            openUser(item.user!!.id)
        }
        userNameTv.setOnClickListener {
            openUser(item.user!!.id)
        }

        activityLikeContainer.setOnClickListener {
            viewModel.toggleActivityLike(item){
                updateLike(item)
            }
        }

        activityReplyContainer.setOnClickListener {
            viewModel.activeActivityUnionModel = item
            OpenActivityInfoEvent(item.id!!).postEvent
        }

        activityMorePopup.setOnClickListener {
            val filteredMenu = activityMenuEntries.filterIndexed { index, s -> if (item.userId == userId) true else index < 2 }.toTypedArray()
            makeArrayPopupMenu(it, filteredMenu) { _, _, position, _ ->
                when (position) {
                    0 -> {
                        context.openLink(item.siteUrl)
                    }
                    1 -> {
                        context.copyToClipBoard(item.siteUrl)
                    }
                    2 -> {
                        viewModel.activeActivityUnionModel = item
                        OpenActivityComposer().postEvent
                    }
                    3 -> {
                        makeConfirmationDialog(context) {
                            context.makeToast(R.string.please_wait)
                            viewModel.deleteActivity(item.id ?: -1) { id, success ->
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

    }

    private fun TextActivityPresenterLayoutBinding.updateLike(item: TextActivityModel) {
        activityLikeCountTv.text = item.likeCount.toString()
        activityLikeIv.setImageResource(if (item.isLiked) R.drawable.ic_activity_like_filled else R.drawable.ic_activity_like_outline)
    }

    private fun ListActivityPresenterLayoutBinding.bind(item: ListActivityModel) {
        userNameTv.text = item.user!!.name

        activityListStatus.text = item.getProgressStatus
        activityLikeCountTv.text = item.likeCount.toString()
        activityReplyCountTv.text = item.replyCount.toString()

        userAvatarIv.setImageURI(item.user!!.avatar?.large)
        mediaCoverIv.setImageURI(item.media!!.coverImage?.image(context))

        activityCreatedAtTv.text = item.createdAt

        userAvatarIv.setOnClickListener {
            openUser(item.user!!.id)
        }
        userNameTv.setOnClickListener {
            openUser(item.user!!.id)
        }
        mediaCoverIv.setOnClickListener {
            openMedia(item.media!!)
        }
        activityListStatus.setOnClickListener {
            openMedia(item.media!!)
        }
        activityMorePopup.setOnClickListener {
            val filteredMenu = activityMenuEntries.filterIndexed { index, s -> if (item.userId == userId) index != 2 else index < 2 }.toTypedArray()
            makeArrayPopupMenu(it, filteredMenu) { _, _, position, _ ->
                when (position) {
                    0 -> {
                        context.openLink(item.siteUrl)
                    }
                    1 -> {
                        context.copyToClipBoard(item.siteUrl)
                    }
                    3 -> {
                        makeConfirmationDialog(context) {
                            context.makeToast(R.string.please_wait)
                            viewModel.deleteActivity(item.id ?: -1) { id, success ->
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
            viewModel.activeActivityUnionModel = item
            OpenActivityInfoEvent(item.id!!).postEvent
        }
    }

    private fun openUser(userId: Int?) {
        OpenUserProfileEvent(userId).postEvent
    }

    private fun openMedia(item: CommonMediaModel) {
        OpenMediaInfoEvent(
            MediaInfoMeta(
                item.mediaId,
                item.type!!,
                item.title!!.userPreferred,
                item.coverImage!!.image(context),
                item.coverImage!!.largeImage,
                item.bannerImage
            )
        ).postEvent
    }
}