package com.revolgenx.anilib.browse.data.service

import com.revolgenx.anilib.browse.data.field.BrowseField
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.character.ui.model.CharacterNameModel
import com.revolgenx.anilib.character.ui.model.toModel
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.fragment.PageInfo
import com.revolgenx.anilib.media.ui.model.MediaConnectionModel
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.staff.ui.model.StaffNameModel
import com.revolgenx.anilib.staff.ui.model.toModel
import com.revolgenx.anilib.studio.ui.model.StudioModel
import com.revolgenx.anilib.user.ui.model.UserModel
import com.revolgenx.anilib.user.ui.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BrowseServiceImpl(repository: ApolloRepository) : BaseService(repository), BrowseService {
    override fun browse(browseField: BrowseField): Flow<PageModel<BaseModel>> {
        return browseField.toQuery().map {
            var data:List<BaseModel>? = null
            var pageInfo: PageInfo? = null
            it.dataAssertNoErrors.apply {
                when {
                    mediaPage != null -> {
                        pageInfo = mediaPage.pageInfo.pageInfo
                        data = mediaPage.media?.mapNotNull { map ->
                            map?.media?.toModel()
                        }
                    }

                    characterPage != null -> {
                        pageInfo = characterPage.pageInfo.pageInfo
                        data = characterPage.characters?.mapNotNull { map ->
                            map?.narrowCharacterContent?.let { character ->
                                CharacterModel(
                                    id = character.id,
                                    name = character.name?.let {
                                        CharacterNameModel(it.full)
                                    },
                                    image = character.image?.characterImage?.toModel()
                                )
                            }
                        }
                    }

                    staffPage != null -> {
                        pageInfo = staffPage.pageInfo.pageInfo
                        data = staffPage.staff?.mapNotNull { map ->
                            map?.narrowStaffContent?.let { staff ->
                                StaffModel(
                                    id = staff.id,
                                    name = staff.name?.let { name ->
                                        StaffNameModel(name.full)
                                    },
                                    image = staff.image?.staffImage?.toModel()
                                )
                            }
                        }
                    }

                    studioPage != null -> {
                        pageInfo = studioPage.pageInfo.pageInfo
                        data = studioPage.studios?.mapNotNull { map ->
                            map?.studioContent?.let { studio ->
                                StudioModel(
                                    id = studio.id,
                                    name = studio.name,
                                    media = studio.media?.let { media ->
                                        MediaConnectionModel(
                                            nodes = media.nodes?.mapNotNull {
                                                it?.takeIf { /*if (field.canShowAdult) true else it.mediaContent.isAdult == false todo filter 18 media*/ true }?.media?.toModel()
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    }

                    userPage != null -> {
                        pageInfo = userPage.pageInfo.pageInfo
                        data = userPage.users?.mapNotNull { map ->
                            map?.let { user ->
                                UserModel(
                                    id = user.id,
                                    name = user.name,
                                    avatar = user.avatar?.userAvatar?.toModel()
                                )
                            }
                        }
                    }

                    else -> {
                        data = emptyList()
                    }
                }
            }
            PageModel(
                pageInfo = pageInfo,
                data = data
            )
        }
    }
}