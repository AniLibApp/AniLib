package com.revolgenx.anilib.friend.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.databinding.UserFriendPresenterLayoutBinding
import com.revolgenx.anilib.common.event.OpenUserProfileEvent
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.user.data.model.UserModel

class UserFriendPresenter(context: Context) :
    BasePresenter<UserFriendPresenterLayoutBinding, UserModel>(context) {
    override val elementTypes: Collection<Int> = listOf(0)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): UserFriendPresenterLayoutBinding {
        return UserFriendPresenterLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onBind(page: Page, holder: Holder, element: Element<UserModel>) {
        super.onBind(page, holder, element)

        val item = element.data ?: return
        holder.getBinding()?.apply {
            userAvatarIv.setImageURI(item.avatar?.image)
            userNameTv.text = item.name
            userMutualChip.visibility = if(item.isMutual) View.VISIBLE else View.GONE
            root.setOnClickListener {
                OpenUserProfileEvent(item.id).postEvent
            }
        }
    }
}