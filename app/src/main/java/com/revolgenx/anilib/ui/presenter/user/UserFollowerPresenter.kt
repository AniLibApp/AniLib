package com.revolgenx.anilib.ui.presenter.user

import android.content.Context
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.infrastructure.event.UserBrowseEvent
import com.revolgenx.anilib.data.model.user.UserFollowersModel
import kotlinx.android.synthetic.main.user_follower_presenter_layout.view.*

class UserFollowerPresenter(requireContext: Context) :
    Presenter<UserFollowersModel>(requireContext) {
    override val elementTypes: Collection<Int> = listOf(0)

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            getLayoutInflater().inflate(
                R.layout.user_follower_presenter_layout,
                parent,
                false
            )
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<UserFollowersModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.itemView.apply {
            userFollowerSimpleDrawee.setImageURI(item.avatar?.medium)
            userFollowerTv.text = item.userName
            setOnClickListener {
                UserBrowseEvent(item.userId).postEvent
            }
        }
    }

}
