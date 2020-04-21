package com.revolgenx.anilib.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.field.user.UserFavouriteField
import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.service.user.UserService
import io.reactivex.disposables.CompositeDisposable

class UserFavouriteSource(
    field: UserFavouriteField,
    private val userService: UserService,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<BaseModel, UserFavouriteField>(field) {
    override fun areItemsTheSame(first: BaseModel, second: BaseModel): Boolean {
        return first.baseId == second.baseId
    }

    override fun getElementType(data: BaseModel): Int {
        return field.favType?.ordinal!!
    }
    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        userService.getUserFavourite(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}
