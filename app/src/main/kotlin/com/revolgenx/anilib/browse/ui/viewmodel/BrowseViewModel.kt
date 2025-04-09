package com.revolgenx.anilib.browse.ui.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.revolgenx.anilib.browse.data.field.BrowseField
import com.revolgenx.anilib.browse.data.field.BrowseTypes
import com.revolgenx.anilib.browse.data.service.BrowseService
import com.revolgenx.anilib.browse.data.source.BrowsePagingSource
import com.revolgenx.anilib.browse.data.store.BrowseFilterData
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.GenreCollectionDataStore
import com.revolgenx.anilib.common.data.store.MediaTagCollectionDataStore
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.media.ui.model.MediaGenreModel
import com.revolgenx.anilib.media.ui.model.MediaTagModel

class BrowseViewModel(
    mediaTagCollectionDataStore: MediaTagCollectionDataStore,
    genreCollectionDataStore: GenreCollectionDataStore,
    private val browseService: BrowseService,
    private val appPreferencesDataStore: AppPreferencesDataStore
) :
    PagingViewModel<BaseModel, BrowseField, BrowsePagingSource>() {

    private val handler = Handler(Looper.getMainLooper())

    private val canShowAdultContent = appPreferencesDataStore.displayAdultContent.get()!!

    private val excludedAnimeTags = mutableListOf<MediaTagModel>()
    private val excludedMangaTags = mutableListOf<MediaTagModel>()
    private val excludedAnimeGenre = mutableListOf<MediaGenreModel>()
    private val excludedMangaGenre = mutableListOf<MediaGenreModel>()

    override var field by mutableStateOf(BrowseField())

    var isExcludedTagsFiltered = false
    var isExcludedGenreFiltered = false

    init {
        mediaTagCollectionDataStore.data.get().tags.map {
            if (it.isAdult && !canShowAdultContent) return@map
            if (it.isExcludedInAnime) {
                excludedAnimeTags += it
            }
            if (it.isExcludedInManga) {
                excludedMangaTags += it
            }
        }


        genreCollectionDataStore.data.get().genre.map {
            if (it.name.contains("hentai", true) && !canShowAdultContent) return@map

            if (it.isExcludedInAnime) {
                excludedAnimeGenre += it
            }
            if (it.isExcludedInManga) {
                excludedMangaGenre += it
            }
        }

        updateExcludedFields()
    }

    private fun updateExcludedFields(){
        val tagsIn = field.tagsIn
        val genreIn = field.genreIn

        if(field.browseType.value == BrowseTypes.ANIME){
            if(!isExcludedTagsFiltered){
                field.tagsNotIn = excludedAnimeTags.map { it.name }.filter { tagsIn == null || !tagsIn.contains(it) }
            }
            if(!isExcludedGenreFiltered){
                field.genreNotIn = excludedAnimeGenre.map { it.name }.filter { genreIn == null || !genreIn.contains(it) }
            }
        }else if(field.browseType.value == BrowseTypes.MANGA){
            if(!isExcludedTagsFiltered) {
                field.tagsNotIn = excludedMangaTags.map { it.name }
                    .filter { tagsIn == null || !tagsIn.contains(it) }
            }
            if(!isExcludedGenreFiltered){
                field.genreNotIn = excludedMangaGenre.map { it.name }.filter { genreIn == null || !genreIn.contains(it) }
            }
        }
    }

    val listType =
        derivedStateOf { if (field.browseType.value == BrowseTypes.STUDIO) ListPagingListType.COLUMN else ListPagingListType.GRID }

    override val pagingSource: BrowsePagingSource
        get() = BrowsePagingSource(this.field, browseService)

    var query by mutableStateOf("")
    var searchQuery: String = ""
        set(value) {
            if (field != value) {
                query = value
                this.field.search = value
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    refresh()
                }, 700)
            }
            field = value
        }
        get() = query

    val searchHistory = mutableStateOf(emptyList<String>())


    companion object {
        private const val searchSplitKeyword = "|#|,"
    }

    init {
        launch {
            appPreferencesDataStore.browseHistory.collect {
                searchHistory.value =
                    it!!.takeIf { it.isNotBlank() }?.split(searchSplitKeyword) ?: emptyList()
            }
        }
    }

    fun search() {
        handler.removeCallbacksAndMessages(null)
        refresh()
    }

    fun updateSearchHistory() {
        val search = field.search?.takeIf { it.isNotBlank() } ?: return
        val history = searchHistory.value.toMutableList()
        history.remove(search)
        history.add(0, search)
        if (history.size > 10) {
            history.removeAt(history.lastIndex)
        }
        launch {
            appPreferencesDataStore.browseHistory.set(history.joinToString(searchSplitKeyword))
        }
    }

    fun deleteSearchHistory(value: String) {
        val history = searchHistory.value.toMutableList()
        history.remove(value)
        launch {
            appPreferencesDataStore.browseHistory.set(history.joinToString(searchSplitKeyword))
        }
    }

    fun setFieldFromBrowseFilterData(browseFilterData: BrowseFilterData) {
        this.field = browseFilterData.toBrowseField()
        updateExcludedFields()
    }

    fun updateBrowseType(browseTypes: BrowseTypes){
        this.field.browseType.value = browseTypes
        updateExcludedFields()
    }

    fun updateFieldFromFilter(field: BrowseField){
        this.field = field
    }
}