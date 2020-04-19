package com.revolgenx.anilib.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.field.user.UserFollowerField
import com.revolgenx.anilib.model.user.UserFollowersModel
import com.revolgenx.anilib.service.user.UserService
import io.reactivex.disposables.CompositeDisposable

class UserFollowersSource(
    field: UserFollowerField,
    private val userService: UserService,
    private val compositeDisposable: CompositeDisposable
) :
    BaseRecyclerSource<UserFollowersModel, UserFollowerField>(field) {
    override fun areItemsTheSame(first: UserFollowersModel, second: UserFollowersModel): Boolean {
        return first.userId == second.userId
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        userService.getFollowersUsers(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}
