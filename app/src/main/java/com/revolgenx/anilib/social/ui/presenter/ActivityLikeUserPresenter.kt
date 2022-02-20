package com.revolgenx.anilib.social.ui.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.user.data.model.UserModel
import com.revolgenx.anilib.databinding.ActivityLikeUserPresenterLayoutBinding
import com.revolgenx.anilib.common.event.OpenUserProfileEvent
import com.revolgenx.anilib.common.presenter.BasePresenter

class ActivityLikeUserPresenter(context: Context) :
    BasePresenter<ActivityLikeUserPresenterLayoutBinding, UserModel>(context) {
    override val elementTypes: Collection<Int> = listOf(0)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): ActivityLikeUserPresenterLayoutBinding {
        return ActivityLikeUserPresenterLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<UserModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return

        holder.getBinding()!!.apply {
            userAvatarIv.setImageURI(item.avatar?.image)
            root.setOnClickListener {
                OpenUserProfileEvent(item.id).postEvent
            }
        }
    }


}
