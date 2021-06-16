package com.revolgenx.anilib.social.ui.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.databinding.ActivityReplyPresenterLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenActivityReplyComposer
import com.revolgenx.anilib.infrastructure.event.OpenUserProfileEvent
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.social.data.model.reply.ActivityReplyModel
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.social.ui.viewmodel.ActivityReplyViewModel
import com.revolgenx.anilib.social.ui.viewmodel.composer.ActivityReplyComposerViewModel
import com.revolgenx.anilib.ui.presenter.BasePresenter
import com.revolgenx.anilib.ui.view.makeToast

class ActivityReplyPresenter(
    context: Context,
    private val activityReplyViewModel: ActivityReplyViewModel,
    private val activityReplyComposerViewModel: ActivityReplyComposerViewModel
) : BasePresenter<ActivityReplyPresenterLayoutBinding, ActivityReplyModel>(context) {

    private val userId by lazy {
        context.userId()
    }

    override val elementTypes: Collection<Int> = listOf(0)
    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): ActivityReplyPresenterLayoutBinding {
        return ActivityReplyPresenterLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<ActivityReplyModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.getBinding()?.apply {
            userAvatarIv.setImageURI(item.user?.avatar?.image)
            userNameTv.text = item.user?.name


            activityReplyCreatedAtTv.text = item.createdAt

            AlMarkwonFactory.getMarkwon().setParsedMarkdown(replyTextTv, item.textSpanned!!)

            if (userId == item.userId) {
                replyEditIv.visibility = View.VISIBLE
                replyEditIv.onPopupMenuClickListener = { _, position ->
                    activityReplyComposerViewModel.activeModel = item
                    OpenActivityReplyComposer().postEvent
                }
            } else {
                replyEditIv.visibility = View.GONE
            }
            updateLike(item)

            listActivityLikeIv.setOnClickListener {
                activityReplyViewModel.toggleLike(item) {
                    if (it.status == Status.ERROR) {
                        context.makeToast(R.string.failed_to_toggle)
                    } else {
                        val likeAbleItem = it.data ?: return@toggleLike
                        if(likeAbleItem.id == item.id){
                            item.isLiked = likeAbleItem.isLiked
                            item.likeCount = likeAbleItem.likeCount
                            updateLike(item)
                        }
                    }
                }
            }

            userAvatarIv.setOnClickListener {
                OpenUserProfileEvent(item.userId).postEvent
            }

            userNameTv.setOnClickListener{
                OpenUserProfileEvent(item.userId).postEvent
            }
        }
    }

    private fun ActivityReplyPresenterLayoutBinding.updateLike(item: ActivityReplyModel) {
        activityReplyLikeCountTv.text = item.likeCount.toString()
        listActivityLikeIv.setImageResource(if (item.isLiked) R.drawable.ic_activity_like_filled else R.drawable.ic_activity_like_outline)
    }
}
