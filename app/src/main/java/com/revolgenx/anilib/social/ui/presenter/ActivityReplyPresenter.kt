package com.revolgenx.anilib.social.ui.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.databinding.ActivityReplyPresenterLayoutBinding
import com.revolgenx.anilib.social.data.model.reply.ActivityReplyModel
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.ui.presenter.BasePresenter

class ActivityReplyPresenter(context: Context):BasePresenter<ActivityReplyPresenterLayoutBinding, ActivityReplyModel>(context) {
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
        val item = element.data?: return
        holder.getBinding()?.apply {
            userAvatarIv.setImageURI(item.user?.avatar?.image)
            userNameTv.text = item.user?.name


            activityReplyLikeCountTv.text = item.likeCount.toString()
            activityReplyCreatedAtTv.text = item.createdAt

            AlMarkwonFactory.getMarkwon().setParsedMarkdown(replyTextTv, item.textSpanned!!)
        }
    }
}
