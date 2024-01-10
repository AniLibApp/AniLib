package com.revolgenx.anilib.browse.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.browse.data.field.BrowseField
import com.revolgenx.anilib.common.data.tuples.MutablePair
import com.revolgenx.anilib.common.data.tuples.to
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.BaseViewModel
import com.revolgenx.anilib.common.data.store.MediaTagCollectionDataStore
import com.revolgenx.anilib.common.data.store.ReadableOnCollectionDataStore
import com.revolgenx.anilib.common.data.store.StreamingOnCollectionDataStore
import com.revolgenx.anilib.media.ui.model.MediaExternalLinkModel
import com.revolgenx.anilib.setting.data.store.MediaSettingsPreferencesDataStore
import kotlinx.coroutines.flow.combine

class BrowseFilterViewModel(
    mediaTagDataStore: MediaTagCollectionDataStore,
    streamingOnCollectionDataStore: StreamingOnCollectionDataStore,
    readableOnCollectionDataStore: ReadableOnCollectionDataStore,
    mediaSettingsPreferencesDataStore: MediaSettingsPreferencesDataStore
) : BaseViewModel<BrowseField>() {

    override val field: BrowseField = BrowseField()

    val mediaTagCollections = mutableStateOf(emptyList<MutablePair<Boolean?, String>>())
    val canShowAdultContent =
        mutableStateOf(mediaSettingsPreferencesDataStore.displayAdultContent.get())
    val streamingOnCollections = mutableStateOf(emptyList<MutablePair<Boolean, String>>())
    val readableOnCollections = mutableStateOf(emptyList<MediaExternalLinkModel>())

    init {
        launch {
            streamingOnCollectionDataStore.data.collect {
                streamingOnCollections.value = it.links.map { false to it.site.orEmpty() }
            }
        }
        launch {
            combine(
                mediaTagDataStore.data,
                mediaSettingsPreferencesDataStore.displayAdultContent.data
            ) { tagCollections, canShowAdult ->
                tagCollections to canShowAdult
            }.collect { (tagCollections, canShowAdult) ->
                canShowAdultContent.value = canShowAdult
                mediaTagCollections.value =
                    tagCollections.tags.filter { if (canShowAdult!!) true else !it.isAdult }
                        .map { null to it.name }
            }
        }
        launch {
            readableOnCollectionDataStore.data.collect {
                readableOnCollections.value = it.links
            }
        }
    }

    fun updateFilter() {

    }
}