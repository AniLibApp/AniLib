package com.revolgenx.anilib.entry.data.service

import com.apollographql.apollo3.api.Optional
import com.revolgenx.anilib.DeleteMediaListEntryMutation
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.entry.data.field.MediaListEntryField
import com.revolgenx.anilib.entry.data.field.SaveMediaListEntryField
import com.revolgenx.anilib.entry.ui.model.UserMediaModel
import com.revolgenx.anilib.entry.ui.model.toModel
import com.revolgenx.anilib.list.ui.model.MediaListModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MediaListEntryServiceImpl(apolloRepository: ApolloRepository, appPreferencesDataStore: AppPreferencesDataStore) : BaseService(apolloRepository, appPreferencesDataStore),
    MediaListEntryService {

    override fun saveMediaListEntry(field: SaveMediaListEntryField): Flow<MediaListModel?> {
        return field.toMutation().map {
            it.dataAssertNoErrors.toModel()
        }
    }

    override fun getMediaListEntry(field: MediaListEntryField): Flow<UserMediaModel> {
        return field.toQuery().map { it.dataAssertNoErrors.toModel() }
    }

    override fun deleteMediaListEntry(id: Int): Flow<Boolean> {
        return apolloRepository.mutation(
            DeleteMediaListEntryMutation(
                listId = Optional.presentIfNotNull(id)
            )
        )
            .map { it.data?.deleteMediaListEntry?.deleted == true }
    }
}