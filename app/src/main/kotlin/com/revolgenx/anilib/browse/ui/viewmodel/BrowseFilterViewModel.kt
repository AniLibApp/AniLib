package com.revolgenx.anilib.browse.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.revolgenx.anilib.browse.data.field.BrowseField
import com.revolgenx.anilib.browse.data.field.BrowseTypes
import com.revolgenx.anilib.common.data.store.BrowseFilterDataStore
import com.revolgenx.anilib.common.data.store.GenreCollectionDataStore
import com.revolgenx.anilib.common.data.store.MediaTagCollectionDataStore
import com.revolgenx.anilib.common.data.store.ReadableOnCollectionDataStore
import com.revolgenx.anilib.common.data.store.StreamingOnCollectionDataStore
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.component.menu.MultiSelectModel
import com.revolgenx.anilib.common.ui.component.menu.SelectFilterModel
import com.revolgenx.anilib.common.ui.component.menu.SelectType
import com.revolgenx.anilib.common.ui.viewmodel.BaseViewModel
import com.revolgenx.anilib.media.ui.model.MediaExternalLinkModel
import com.revolgenx.anilib.media.ui.model.MediaTagModel
import com.revolgenx.anilib.setting.data.store.MediaSettingsPreferencesDataStore
import kotlinx.coroutines.flow.first

class BrowseFilterViewModel(
    mediaTagCollectionDataStore: MediaTagCollectionDataStore,
    genreCollectionDataStore: GenreCollectionDataStore,
    streamingOnCollectionDataStore: StreamingOnCollectionDataStore,
    readableOnCollectionDataStore: ReadableOnCollectionDataStore,
    private val browseFilterDataStore: BrowseFilterDataStore,
    mediaSettingsPreferencesDataStore: MediaSettingsPreferencesDataStore
) : BaseViewModel<BrowseField>() {

    override var field: BrowseField by mutableStateOf(BrowseField())

    val canShowAdultContent =
        mutableStateOf(mediaSettingsPreferencesDataStore.displayAdultContent.get())
    val selectStreamingOnCollections =
        mutableStateOf(emptyList<MultiSelectModel<MediaExternalLinkModel>>())
    val selectReadableOnCollections =
        mutableStateOf(emptyList<MultiSelectModel<MediaExternalLinkModel>>())

    val selectMediaTagCollections =
        mutableStateOf(emptyList<SelectFilterModel<String>>())
    val selectGenreCollections =
        mutableStateOf(emptyList<SelectFilterModel<String>>())

    private var mediaTagCollections = emptyList<MediaTagModel>()
    private var genreCollections = emptyList<String>()
    private var streamingOnCollections = emptyList<MediaExternalLinkModel>()
    private var readableOnCollections = emptyList<MediaExternalLinkModel>()

    init {
        launch {
            mediaSettingsPreferencesDataStore.displayAdultContent.data.collect { canShowAdult ->
                canShowAdultContent.value = canShowAdult

                launch {
                    mediaTagCollectionDataStore.data.collect { tagCollections ->
                        mediaTagCollections =
                            tagCollections.tags.filter { if (canShowAdult!!) true else !it.isAdult }
                        updateSelectMediaTagCollections()
                    }
                }

                launch {
                    genreCollectionDataStore.data.collect { collections ->
                        genreCollections =
                            collections.genre.filter { if (canShowAdult!!) true else it != "Hentai" }
                        updateSelectGenreCollections()
                    }
                }
            }
        }


        launch {
            streamingOnCollectionDataStore.data.collect {
                streamingOnCollections = it.links
                updateSelectStreamingOnCollections()
            }
        }
        launch {
            readableOnCollectionDataStore.data.collect {
                readableOnCollections = it.links
                updateSelectReadableOnCollections()
            }
        }
    }

    fun reloadPrevious() {
        launch {
            updateField(browseFilterDataStore.data.first().toBrowseField())
        }
    }

    fun clearFilter() {
        updateField(BrowseField(search = field.search))
    }

    fun updateFilter() {
        launch {
            browseFilterDataStore.updateData {
                field.toBrowseFilter()
            }
        }
    }

    fun updateField(mField: BrowseField? = null) {
        if (mField == null) {
            updateUIFilters()
        } else if (mField != field) {
            this.field = mField
            updateUIFilters()
        }
    }

    private fun updateUIFilters() {
        updateSelectMediaTagCollections()
        updateSelectGenreCollections()

        when (field.browseType.value) {
            BrowseTypes.ANIME -> {
                updateSelectStreamingOnCollections()
            }

            BrowseTypes.MANGA -> {
                updateSelectReadableOnCollections()
            }

            else -> {}
        }
    }

    private fun updateSelectGenreCollections() {
        selectGenreCollections.value =
            genreCollections.map {
                val included =
                    if (field.genreIn?.contains(it) == true) SelectType.INCLUDED
                    else if (field.genreNotIn?.contains(it) == true) SelectType.EXCLUDED
                    else SelectType.NONE
                SelectFilterModel(selected = mutableStateOf(included), data = it)
            }
    }

    private fun updateSelectMediaTagCollections() {
        selectMediaTagCollections.value = mediaTagCollections.map {
            val included =
                if (field.tagsIn?.contains(it.name) == true) SelectType.INCLUDED
                else if (field.tagsNotIn?.contains(it.name) == true) SelectType.EXCLUDED
                else SelectType.NONE
            SelectFilterModel(selected = mutableStateOf(included), data = it.name)
        }
    }

    private fun updateSelectStreamingOnCollections() {
        selectStreamingOnCollections.value = streamingOnCollections.map { linkModel ->
            MultiSelectModel(
                selected = mutableStateOf(field.streamingOn?.contains(linkModel.id) == true),
                linkModel
            )
        }
    }

    private fun updateSelectReadableOnCollections() {
        selectReadableOnCollections.value = readableOnCollections.map { linkModel ->
            MultiSelectModel(
                selected = mutableStateOf(field.readableOn?.contains(linkModel.id) == true),
                linkModel
            )
        }
    }
}