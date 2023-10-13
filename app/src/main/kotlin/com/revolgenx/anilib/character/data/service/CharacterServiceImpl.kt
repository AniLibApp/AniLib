package com.revolgenx.anilib.character.data.service

import com.revolgenx.anilib.character.data.field.CharacterActorField
import com.revolgenx.anilib.character.data.field.CharacterField
import com.revolgenx.anilib.character.data.field.CharacterMediaField
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.character.ui.model.toModel
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.ext.onIO
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.setting.data.store.MediaSettingsDataStore
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.staff.ui.model.StaffNameModel
import com.revolgenx.anilib.staff.ui.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharacterServiceImpl(
    apolloRepository: ApolloRepository,
    mediaSettingsDataStore: MediaSettingsDataStore
) : BaseService(apolloRepository, mediaSettingsDataStore),
    CharacterService {

    override fun getCharacter(field: CharacterField): Flow<CharacterModel?> {
        return field.toQuery().map { it.dataAssertNoErrors.character?.toModel() }
            .onIO()
    }

    override fun getCharacterMedia(field: CharacterMediaField): Flow<PageModel<MediaModel>> {
        return field.toQuery().map {
            it.dataAssertNoErrors.let { data ->
                PageModel(
                    data = data.character?.media?.nodes?.mapNotNull {
                        it?.onMedia?.media?.toModel()
                    },
                    pageInfo = data.character?.media?.pageInfo?.pageInfo
                )

            }
        }
            .onIO()
    }

    override fun getCharacterActor(field: CharacterActorField): Flow<List<StaffModel>?> {
        return field.toQuery().map {
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
}