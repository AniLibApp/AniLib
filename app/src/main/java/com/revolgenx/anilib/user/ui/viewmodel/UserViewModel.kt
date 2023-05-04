package com.revolgenx.anilib.user.ui.viewmodel

import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.user.data.field.UserField
import com.revolgenx.anilib.user.data.service.UserService
import com.revolgenx.anilib.user.ui.model.UserModel
import kotlinx.coroutines.flow.Flow

class UserViewModel(
    private val userService: UserService,
    val appPreferencesDataStore: AppPreferencesDataStore
) :
    ResourceViewModel<UserModel, UserField>() {
    override val field: UserField = UserField()
    override fun loadData(): Flow<UserModel?> = userService.getUser(field)
}