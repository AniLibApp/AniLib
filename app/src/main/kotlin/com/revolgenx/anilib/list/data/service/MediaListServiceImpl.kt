package com.revolgenx.anilib.list.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.list.data.field.MediaListCollectionIdField
import com.revolgenx.anilib.list.data.field.MediaListField
import com.revolgenx.anilib.list.ui.model.MediaListCollectionModel
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.list.ui.model.toModel
import com.revolgenx.anilib.user.ui.model.MediaListOptionModel
import com.revolgenx.anilib.user.ui.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MediaListServiceImpl(
    apolloRepository: ApolloRepository,
    appPreferencesDataStore: AppPreferencesDataStore
) : MediaListService,
    BaseService(apolloRepository, appPreferencesDataStore) {

    override fun getMediaListCollectionsIds(field: MediaListCollectionIdField): Flow<List<Int>> {
        return field.toQuery().mapData {
            it.dataAssertNoErrors.mediaListCollection?.lists?.mapNotNull { list ->
                list?.entries?.mapNotNull { entry ->
                    entry?.takeIf { if (field.canShowAdult) true else it.media?.isAdult == false }?.media?.id
                }
            }?.flatten().orEmpty()
        }
    }

    override fun getMediaListCollection(field: MediaListCollectionField): Flow<MediaListCollectionModel> {
        return field.toQuery().mapData { it.dataAssertNoErrors.toModel(field) }
    }

    override fun getMediaList(field: MediaListField): Flow<PageModel<MediaListModel>> =
        field.toQuery()
            .mapData {
                it.dataAssertNoErrors.let { data ->
                    data.page.let { page ->
                        PageModel(
                            pageInfo = page.pageInfo.pageInfo,
                            data = page.mediaList?.mapNotNull { list ->
                                list?.takeIf { if (field.canShowAdult) true else it.media?.media?.isAdult == false }
                                    ?.toModel()?.copy(
                                        user = data.user?.let {
                                            UserModel(mediaListOptions = it.mediaListOptions?.let { option ->
                                                MediaListOptionModel(scoreFormat = option.scoreFormat)
                                            })
                                        }
                                    )
                            }
                        )
                    }
                }
            }
}