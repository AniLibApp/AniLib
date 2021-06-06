package com.revolgenx.anilib.ui.presenter.user

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.data.model.user.UserFollowersModel
import com.revolgenx.anilib.databinding.UserFollowerPresenterLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenUserProfileEvent
import com.revolgenx.anilib.ui.presenter.BasePresenter

class UserFollowerPresenter(requireContext: Context) :
    BasePresenter<UserFollowerPresenterLayoutBinding, UserFollowersModel>(requireContext) {
    override val elementTypes: Collection<Int> = listOf(0)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): UserFollowerPresenterLayoutBinding {
        return UserFollowerPresenterLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<UserFollowersModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        val binding = holder.getBinding() ?:return

        binding.apply {
            userFollowerSimpleDrawee.setImageURI(item.avatar?.medium)
            userFollowerTv.text = item.name
            root.setOnClickListener {
                OpenUserProfileEvent(item.id).postEvent
            }
        }
    }

}
