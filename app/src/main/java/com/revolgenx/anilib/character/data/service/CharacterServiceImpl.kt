package com.revolgenx.anilib.character.data.service

import com.revolgenx.anilib.character.data.field.CharacterField
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.character.ui.model.toModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class CharacterServiceImpl(apolloRepository: ApolloRepository) : BaseService(apolloRepository),
    CharacterService {

    override fun getCharacter(field: CharacterField): Flow<CharacterModel?> {
        return field.toQuery().map { it.dataAssertNoErrors.character?.toModel() }
            .flowOn(Dispatchers.IO)
    }
}