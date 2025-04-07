package com.revolgenx.anilib.setting.data.service

import androidx.compose.runtime.mutableStateOf
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.revolgenx.anilib.TagsAndGenreCollectionQuery
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.GenreCollectionDataStore
import com.revolgenx.anilib.common.data.store.MediaTagCollectionDataStore
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.media.ui.model.MediaGenreModel
import com.revolgenx.anilib.media.ui.model.MediaTagModel
import com.revolgenx.anilib.setting.data.field.MediaListSettingsField
import com.revolgenx.anilib.setting.data.field.MediaSettingsField
import com.revolgenx.anilib.setting.data.field.NotificationSettingsField
import com.revolgenx.anilib.setting.data.field.SaveMediaListSettingsField
import com.revolgenx.anilib.setting.data.field.SaveMediaSettingsField
import com.revolgenx.anilib.setting.data.field.SaveNotificationSettingsField
import com.revolgenx.anilib.setting.ui.model.MediaListSettingsModel
import com.revolgenx.anilib.setting.ui.model.MediaSettingsModel
import com.revolgenx.anilib.setting.ui.model.toModel
import com.revolgenx.anilib.type.NotificationType
import com.revolgenx.anilib.user.ui.model.toModel
import kotlinx.coroutines.flow.Flow

class SettingsServiceImpl(
    apolloRepository: ApolloRepository,
    appPreferencesDataStore: AppPreferencesDataStore,
    private val tagsCollectionDataStore: MediaTagCollectionDataStore,
    private val genreCollectionDataStore: GenreCollectionDataStore,
) : BaseService(apolloRepository, appPreferencesDataStore),
    SettingsService {
    override fun getNotificationSettings(field: NotificationSettingsField): Flow<Map<NotificationType, Boolean>?> {
        return field.toQuery().mapData {
            it.data?.user?.options?.notificationOptions?.filterNotNull()
                ?.associateBy({ it.type!! }, { it.enabled == true })
        }
    }

    override fun saveNotificationSettings(field: SaveNotificationSettingsField): Flow<Boolean> {
        return field.toMutation().mapData { it.dataAssertNoErrors.updateUser?.id != null }
    }

    override fun getMediaSettings(field: MediaSettingsField): Flow<MediaSettingsModel?> {
        return field.toQuery()
            .mapData {
                it.dataAssertNoErrors.user?.options?.userMediaOptions?.toModel()?.let {
                    MediaSettingsModel(
                        mutableStateOf(it)
                    )
                }
            }
    }

    override fun saveMediaSettings(field: SaveMediaSettingsField): Flow<Boolean> {
        return field.toMutation().mapData { it.dataAssertNoErrors.updateUser?.id != null }
    }

    override fun getMediaListSettings(field: MediaListSettingsField): Flow<MediaListSettingsModel?> {
        return field.toQuery()
            .mapData {
                it.dataAssertNoErrors.user?.mediaListOptions?.userMediaListOptions?.toModel()?.toModel()
            }
    }

    override fun saveMediaListSettings(field: SaveMediaListSettingsField): Flow<Boolean> {
        return field.toMutation().mapData { it.dataAssertNoErrors.updateUser?.id != null }
    }

    override fun refreshTagsAndGenreCollection(): Flow<Unit> {
        return apolloRepository.query(TagsAndGenreCollectionQuery()).mapData {
            it.dataAssertNoErrors.let { tagsAndGenre ->
                val newTags = tagsAndGenre.MediaTagCollection?.mapNotNull { it?.let { MediaTagModel(name = it.name, isAdult = it.isAdult == true) } }.orEmpty()
                val currentTags = tagsCollectionDataStore.get().tags.toMutableList()

                val existingTagNames = currentTags.map { it.name.lowercase() }.toSet()
                val newTagNames = newTags.map { it.name.lowercase() }.toSet()


                val tagsToAdd = newTags.filter { it.name.lowercase() !in existingTagNames }

                val newGenres = tagsAndGenre.GenreCollection?.mapNotNull { it?.let { MediaGenreModel(name = it) } }.orEmpty()
                val currentGenre = genreCollectionDataStore.get().genre.toMutableList()

                val existingGenreNames = currentGenre.map { it.name.lowercase() }.toSet()
                val newGenreNames = newGenres.map { it.name.lowercase() }.toSet()

                val genreToAdd = newGenres.filter { it.name.lowercase() !in existingGenreNames }

                val tagsRemoved = currentTags.removeAll { it.name.lowercase() !in newTagNames }
                val genreRemoved = currentGenre.removeAll { it.name.lowercase() !in newGenreNames }


                currentTags.addAll(tagsToAdd)
                currentGenre.addAll(genreToAdd)


                currentTags.sortBy { it.name }
                currentGenre.sortBy { it.name }

                tagsCollectionDataStore.updateData {
                    it.copy(tags = currentTags.toList())
                }

                genreCollectionDataStore.updateData {
                    it.copy(genre = currentGenre.toList())
                }


                //log event to know any tags or genre changes.
                if(tagsRemoved || genreRemoved){
                    Firebase.analytics.logEvent("settings_tags_genre") {
                        param("type", "removed")
                    }
                }

                if(tagsToAdd.isNotEmpty() || genreToAdd.isNotEmpty()){
                    Firebase.analytics.logEvent("settings_tags_genre") {
                        param("type", "added")
                    }
                }
            }
        }
    }
}