package com.revolgenx.anilib.user.data.service

import com.revolgenx.anilib.character.ui.model.toModel
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.ext.isNotNull
import com.revolgenx.anilib.common.ext.onIO
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.fragment.PageInfo
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.staff.ui.model.toModel
import com.revolgenx.anilib.studio.ui.model.toModel
import com.revolgenx.anilib.user.data.field.UserFavouriteField
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

    override fun getUserFavourite(field: UserFavouriteField): Flow<PageModel<BaseModel>> {
        return field.toQuery().map {
            val fav = it.dataAssertNoErrors.user?.favourites
            val (pageInfo, data) = when {
                fav?.anime != null -> {
                    fav.anime.pageInfo.pageInfo to fav.anime.nodes?.mapNotNull {
                        it?.onMedia?.media?.toModel()
                    }
                }

                fav?.manga != null -> {
                    fav.manga.pageInfo.pageInfo to fav.manga.nodes?.mapNotNull {
                        it?.onMedia?.media?.toModel()
                    }
                }

                fav?.characters != null -> {
                    fav.characters.pageInfo.pageInfo to fav.characters.nodes?.mapNotNull {
                        it?.onCharacter?.smallCharacter?.toModel()
                    }
                }

                fav?.staff != null -> {
                    fav.staff.pageInfo.pageInfo to fav.staff.nodes?.mapNotNull {
                        it?.onStaff?.smallStaff?.toModel()
                    }
                }

                fav?.studios != null -> {
                    fav.studios.pageInfo.pageInfo to fav.studios.nodes?.mapNotNull {
                        it?.onStudio?.studio?.toModel()
                    }
                }

                else -> null to emptyList()
            }

            PageModel(
                pageInfo = pageInfo,
                data = data
            )
        }.onIO()
    }
}