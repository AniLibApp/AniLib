package com.revolgenx.anilib.character.data.service

import com.revolgenx.anilib.character.data.field.CharacterActorField
import com.revolgenx.anilib.character.data.field.CharacterField
import com.revolgenx.anilib.character.data.field.CharacterMediaField
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.character.ui.model.toModel
import com.revolgenx.anilib.common.data.field.ToggleFavoriteField
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.data.service.ToggleService
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.staff.ui.model.StaffNameModel
import com.revolgenx.anilib.staff.ui.model.toModel
import kotlinx.coroutines.flow.Flow

class CharacterServiceImpl(
    apolloRepository: ApolloRepository,
    appPreferencesDataStore: AppPreferencesDataStore,
    private val toggleService: ToggleService
) : BaseService(apolloRepository, appPreferencesDataStore),
    CharacterService {

    override fun getCharacter(field: CharacterField): Flow<CharacterModel?> {
        return field.toQuery().mapData { it.dataAssertNoErrors.character?.toModel() }
            
    }

    override fun getCharacterMedia(field: CharacterMediaField): Flow<PageModel<MediaModel>> {
        return field.toQuery().mapData {
            it.dataAssertNoErrors.let { data ->
                PageModel(
                    data = data.character?.media?.nodes?.mapNotNull {
                        it?.onMedia?.media?.toModel()
                    },
                    pageInfo = data.character?.media?.pageInfo?.pageInfo
                )

            }
        }
            
    }

    override fun getCharacterActor(field: CharacterActorField): Flow<List<StaffModel>?> {
        return field.toQuery().mapData {
            val hashMap = mutableMapOf<Int, StaffModel>()
            it.dataAssertNoErrors.character?.media?.edges?.forEach {
                it?.voiceActors?.filterNotNull()?.forEach { actor ->
                    hashMap[actor.id] = StaffModel(
                        id = actor.id,
                        name = StaffNameModel(full = actor.name?.full),
                        languageV2 = actor.languageV2,
                        image = actor.image?.staffImage?.toModel()
                    )
                }
            }
            hashMap.values.toList()
        }
    }


    override fun toggleFavorite(characterId: Int): Flow<Boolean> {
        return toggleService.toggleFavourite(ToggleFavoriteField(characterId = characterId))
    }
}