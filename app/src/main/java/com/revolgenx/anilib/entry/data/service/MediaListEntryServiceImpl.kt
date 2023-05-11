package com.revolgenx.anilib.entry.data.service

import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.entry.data.field.MediaListEntryField
import com.revolgenx.anilib.entry.ui.model.UserMediaModel
import com.revolgenx.anilib.entry.ui.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MediaListEntryServiceImpl(apolloRepository: ApolloRepository) : BaseService(apolloRepository),
    MediaListEntryService {
    override fun getMediaListEntry(field: MediaListEntryField): Flow<UserMediaModel> {
        return field.toQuery().map { it.dataAssertNoErrors.toModel() }
    }
}