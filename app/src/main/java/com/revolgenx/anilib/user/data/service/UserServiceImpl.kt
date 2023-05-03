package com.revolgenx.anilib.user.data.service

import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.ext.onIO
import com.revolgenx.anilib.user.data.field.UserField
import com.revolgenx.anilib.user.ui.model.UserModel
import com.revolgenx.anilib.user.ui.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class UserServiceImpl(apolloRepository: ApolloRepository) :
    UserService, BaseService(apolloRepository) {
    override fun getUser(field: UserField): Flow<UserModel?> {
        return field.toQuery().map {
            it.dataAssertNoErrors.let { data ->
                data.user?.toModel()?.also {
                    it.followers = data.followerPage?.pageInfo?.total ?: 0
                    it.following = data.followingPage?.pageInfo?.total ?: 0
                }
            }
        }.onIO()
    }
}