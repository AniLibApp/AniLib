package com.revolgenx.anilib.user.data.service

import com.revolgenx.anilib.user.data.field.UserField
import com.revolgenx.anilib.user.ui.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserService {
    fun getUser(field: UserField): Flow<UserModel?>
}