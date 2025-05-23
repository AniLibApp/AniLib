package com.revolgenx.anilib.staff.data.service

import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.character.ui.model.CharacterNameModel
import com.revolgenx.anilib.character.ui.model.toModel
import com.revolgenx.anilib.common.data.field.ToggleFavoriteField
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.data.service.ToggleService
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.fragment.PageInfo
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.staff.data.field.StaffField
import com.revolgenx.anilib.staff.data.field.StaffMediaCharacterField
import com.revolgenx.anilib.staff.data.field.StaffMediaRoleField
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.staff.ui.model.toModel
import kotlinx.coroutines.flow.Flow

class StaffServiceImpl(
    apolloRepository: ApolloRepository,
    appPreferencesDataStore: AppPreferencesDataStore,
    private val toggleService: ToggleService
) : StaffService,
    BaseService(apolloRepository, appPreferencesDataStore) {
    override fun getStaff(field: StaffField): Flow<StaffModel?> {
        return field.toQuery().mapData { it.dataAssertNoErrors.staff?.toModel() }
            
    }

    override fun getStaffMediaCharacter(field: StaffMediaCharacterField): Flow<PageModel<MediaModel>> {
        return field.toQuery().mapData {

            var pageInfo: PageInfo? = null
            var data: List<MediaModel>? = null

            it.dataAssertNoErrors.staff.let { staff ->
                staff?.characterMedia?.let { characterMedia ->
                    pageInfo = characterMedia.pageInfo.pageInfo
                    data = characterMedia.edges?.mapNotNull { edge ->
                        edge?.characters?.mapNotNull { character ->
                            edge.node?.onMedia?.media?.toModel()?.also { mediaModel ->
                                mediaModel.character = character?.let {
                                    CharacterModel(
                                        id = character.id,
                                        name = CharacterNameModel(character.name?.full),
                                        image = character.image?.characterImage?.toModel(),
                                    )
                                }
                                mediaModel.characterRole = edge.characterRole
                            }
                        }
                    }?.flatten()
                }

                staff?.characters?.let { characters ->
                    pageInfo = characters.pageInfo.pageInfo
                    data = characters.edges?.mapNotNull { edge ->
                        edge?.media?.mapNotNull { media ->
                            media?.onMedia?.media?.toModel()?.also { mediaModel ->
                                mediaModel.character = edge.node?.let { c ->
                                    CharacterModel(
                                        id = c.id,
                                        name = CharacterNameModel(c.name?.full),
                                        image = c.image?.characterImage?.toModel(),
                                    )
                                }
                                mediaModel.characterRole = edge.role
                            }
                        }
                    }?.flatten()
                }

            }

            PageModel(
                pageInfo = pageInfo,
                data = data
            )
        }
    }


    override fun getStaffMediaRole(field: StaffMediaRoleField): Flow<PageModel<MediaModel>> {
        return field.toQuery().mapData {
            val staffMedia = it.dataAssertNoErrors.staff?.staffMedia
            PageModel(
                pageInfo = staffMedia?.pageInfo?.pageInfo,
                data = staffMedia?.edges?.mapNotNull { edge ->
                    edge?.node?.onMedia?.media?.toModel()?.also { model ->
                        model.staffRole = edge.staffRole
                    }
                }
            )
        }
    }

    override fun toggleFavorite(staffId: Int): Flow<Boolean> {
        return toggleService.toggleFavourite(ToggleFavoriteField(staffId = staffId))
    }
}