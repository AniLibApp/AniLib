package com.revolgenx.anilib.browse.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.revolgenx.anilib.browse.data.field.BrowseField
import com.revolgenx.anilib.browse.data.field.BrowseTypes
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.BrowseFilterDataStore
import com.revolgenx.anilib.common.data.store.GenreCollectionDataStore
import com.revolgenx.anilib.common.data.store.MediaTagCollectionDataStore
import com.revolgenx.anilib.common.data.store.ReadableOnCollectionDataStore
import com.revolgenx.anilib.common.data.store.StreamingOnCollectionDataStore
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.component.menu.MultiSelectModel
import com.revolgenx.anilib.common.ui.component.menu.SelectFilterModel
import com.revolgenx.anilib.common.ui.component.menu.SelectType
import com.revolgenx.anilib.common.ui.viewmodel.BaseViewModel
import com.revolgenx.anilib.media.ui.model.MediaExternalLinkModel
import kotlinx.coroutines.flow.first

class BrowseFilterViewModel(
    mediaTagCollectionDataStore: MediaTagCollectionDataStore,
    genreCollectionDataStore: GenreCollectionDataStore,
    streamingOnCollectionDataStore: StreamingOnCollectionDataStore,
    readableOnCollectionDataStore: ReadableOnCollectionDataStore,
    private val browseFilterDataStore: BrowseFilterDataStore,
    appPreferencesDataStore: AppPreferencesDataStore
) : BaseViewModel<BrowseField>() {

    override var field: BrowseField by mutableStateOf(BrowseField())
    val canShowAdultContent = appPreferencesDataStore.displayAdultContent.get()!!
    val isLoggedIn = appPreferencesDataStore.isLoggedIn.get()

    private var mediaTagCollections =
        mediaTagCollectionDataStore.data.get().tags.filter { if (canShowAdultContent) true else !it.isAdult }
    private val genreCollections = genreCollectionDataStore.data.get().genre.filter {
        if (canShowAdultContent) true else !it.name.contains(
            "hentai",
            ignoreCase = true
        )
    }
    private val streamingOnCollections = streamingOnCollectionDataStore.data.get().links
    private val readableOnCollections = readableOnCollectionDataStore.data.get().links

    private val excludedAnimeTags = mediaTagCollections.filter { it.isExcludedInAnime }
    private val excludedMangaTags = mediaTagCollections.filter { it.isExcludedInManga }
    private val excludedAnimeGenre = genreCollections.filter { it.isExcludedInAnime }
    private val excludedMangaGenre = genreCollections.filter { it.isExcludedInManga }

    val selectMediaTagCollections =
        mutableStateOf(emptyList<SelectFilterModel<String>>())
    val selectStreamingOnCollections =
        mutableStateOf(emptyList<MultiSelectModel<MediaExternalLinkModel>>())
    val selectReadableOnCollections =
        mutableStateOf(emptyList<MultiSelectModel<MediaExternalLinkModel>>())
    val selectGenreCollections =
        mutableStateOf(emptyList<SelectFilterModel<String>>())

    var isExcludedTagsFiltered = false
    var isExcludedGenreFiltered = false

    init {
        updateSelectMediaTagCollections()
        updateSelectGenreCollections()
        updateSelectStreamingOnCollections()
        updateSelectReadableOnCollections()
    }

    fun reloadPrevious() {
        launch {
            val previousField = browseFilterDataStore.data.first().toBrowseField()
            val browseType = previousField.browseType.value
            if (browseType == BrowseTypes.ANIME || browseType == BrowseTypes.MANGA) {
                if (!isExcludedTagsFiltered) {
                    val excludedTags =
                        if (browseType == BrowseTypes.ANIME) excludedAnimeTags else excludedMangaTags
                    previousField.tagsIn = previousField.tagsIn?.filter { tagIn ->
                        excludedTags.find {
                            it.name.contains(
                                tagIn,
                                true
                            )
                        } == null
                    }

                    val tagsNotIn = excludedTags.map { it.name }.toMutableList()
                    previousField.tagsNotIn?.forEach {
                        if(!tagsNotIn.contains(it)){
                            tagsNotIn.add(it)
                        }
                    }

                    previousField.tagsNotIn = tagsNotIn
                }


                if (!isExcludedGenreFiltered) {
                    val excludedGenre =
                        if (browseType == BrowseTypes.ANIME) excludedAnimeGenre else excludedMangaGenre
                    previousField.genreIn = previousField.genreIn?.filter { genreIn ->
                        excludedGenre.find {
                            it.name.contains(
                                genreIn,
                                true
                            )
                        } == null
                    }

                    val genreNotIn = excludedGenre.map { it.name }.toMutableList()
                    previousField.genreNotIn?.forEach {
                        if(!genreNotIn.contains(it)){
                            genreNotIn.add(it)
                        }
                    }
                    previousField.genreNotIn = genreNotIn
                }
            }
            updateField(previousField)
        }
    }

    fun clearFilter() {
        updateField(BrowseField(search = field.search))
    }

    fun updateFilter() {
        launch {
            if(field.browseType.value == BrowseTypes.ANIME || field.browseType.value == BrowseTypes.MANGA){
                browseFilterDataStore.updateData {
                    field.toBrowseFilter()
                }
            }
        }
    }

    fun updateField(mField: BrowseField) {
        field = mField
        updateUIFilters()
    }

    fun updateBrowseType(browseTypes: BrowseTypes) {
        field.browseType.value = browseTypes
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
                    if (field.genreIn?.contains(it.name) == true) SelectType.INCLUDED
                    else if (field.genreNotIn?.contains(it.name) == true) SelectType.EXCLUDED
                    else SelectType.NONE
                SelectFilterModel(selected = mutableStateOf(included), data = it.name)
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