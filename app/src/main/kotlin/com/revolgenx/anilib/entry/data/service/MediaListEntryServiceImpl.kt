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
import com.revolgenx.anilib.type.MediaListStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class MediaListEntryServiceImpl(apolloRepository: ApolloRepository, appPreferencesDataStore: AppPreferencesDataStore) : BaseService(apolloRepository, appPreferencesDataStore),
    MediaListEntryService {

    override fun saveMediaListEntry(field: SaveMediaListEntryField): Flow<MediaListModel?> {
        return field.toMutation().mapData {
            it.dataAssertNoErrors.toModel()
        }
    }

    override fun getMediaListEntry(field: MediaListEntryField): Flow<UserMediaModel> {
        return field.toQuery().mapData { it.dataAssertNoErrors.toModel() }
    }

    override fun deleteMediaListEntry(id: Int): Flow<Boolean> {
        return apolloRepository.mutation(
            DeleteMediaListEntryMutation(
                listId = Optional.presentIfNotNull(id)
            )
        )
            .map { it.data?.deleteMediaListEntry?.deleted == true }
    }


    override fun increaseProgress(mediaList: MediaListModel): Flow<MediaListModel?> {
        val oldProgress = mediaList.progress
        val newProgress = (oldProgress ?: 0) + 1
        val episodesOrChapters = mediaList.media?.totalEpisodesOrChapters

        if (episodesOrChapters != null && newProgress > episodesOrChapters) return emptyFlow()

        val progressSaveField = SaveMediaListEntryField().also {
            it.id = mediaList.id
            it.progress = newProgress

            if(mediaList.status.value == MediaListStatus.PLANNING && (oldProgress == null || oldProgress == 0)){
                it.status = MediaListStatus.CURRENT
            }

            if(mediaList.status.value == MediaListStatus.CURRENT && newProgress == episodesOrChapters){
                it.status = MediaListStatus.COMPLETED
            }
        }
        return saveMediaListEntry(progressSaveField)
    }
}