package com.revolgenx.anilib.browse.data.service

import com.revolgenx.anilib.browse.data.field.BrowseField
import com.revolgenx.anilib.character.ui.model.toModel
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.fragment.PageInfo
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.staff.ui.model.toModel
import com.revolgenx.anilib.studio.ui.model.toModel
import com.revolgenx.anilib.user.ui.model.UserModel
import com.revolgenx.anilib.user.ui.model.toModel
import kotlinx.coroutines.flow.Flow

class BrowseServiceImpl(
    apolloRepository: ApolloRepository,
    appPreferencesDataStore: AppPreferencesDataStore
) : BaseService(apolloRepository, appPreferencesDataStore), BrowseService {
    override fun browse(browseField: BrowseField): Flow<PageModel<BaseModel>> {
        return browseField.toQuery().mapData {
            var data: List<BaseModel>? = null
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
                            map?.smallCharacter?.toModel()
                        }
                    }

                    staffPage != null -> {
                        pageInfo = staffPage.pageInfo.pageInfo
                        data = staffPage.staff?.mapNotNull { map ->
                            map?.smallStaff?.toModel()
                        }
                    }

                    studioPage != null -> {
                        pageInfo = studioPage.pageInfo.pageInfo
                        data = studioPage.studios?.mapNotNull { map ->
                            map?.studio?.toModel(field = browseField)
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