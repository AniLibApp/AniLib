package com.revolgenx.anilib.media.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.GenreCollectionDataStore
import com.revolgenx.anilib.common.data.store.MediaTagCollectionDataStore
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.component.menu.SelectFilterModel
import com.revolgenx.anilib.common.ui.component.menu.SelectType
import com.revolgenx.anilib.common.ui.viewmodel.BaseViewModel
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.data.store.MediaFilterData

abstract class MediaFilterBottomSheetViewModel(
    private val mediaFilterDataDataStore: DataStore<MediaFilterData>,
    mediaTagCollectionDataStore: MediaTagCollectionDataStore,
    genreCollectionDataStore: GenreCollectionDataStore,
    appPreferencesDataStore: AppPreferencesDataStore
) : BaseViewModel<MediaField>() {
    private var filter = mediaFilterDataDataStore.data.get()
    override var field: MediaField = filter.toMediaField()

    init {
        launch {
            mediaFilterDataDataStore.data.collect { newFilter ->
                if (filter == newFilter) return@collect
                filter = newFilter
                field = filter.toMediaField()
            }
        }
    }

    private val canShowAdultContent = appPreferencesDataStore.displayAdultContent.get()!!

    private var mediaTagCollections =
        mediaTagCollectionDataStore.data.get().tags.filter { if (canShowAdultContent) true else !it.isAdult }
    private val genreCollections = genreCollectionDataStore.data.get().genre.filter {
        if (canShowAdultContent) true else !it.name.contains(
            "hentai",
            ignoreCase = true
        )
    }


    val selectMediaTagCollections: List<SelectFilterModel<String>> = mediaTagCollections.map {
        val included =
            if (field.tagIn?.contains(it.name) == true) SelectType.INCLUDED
            else if (field.tagNotIn?.contains(it.name) == true) SelectType.EXCLUDED
            else SelectType.NONE
        SelectFilterModel(selected = mutableStateOf(included), data = it.name)
    }

    val selectGenreCollections: List<SelectFilterModel<String>> = genreCollections.map {
            val included =
                if (field.genreIn?.contains(it.name) == true) SelectType.INCLUDED
                else if (field.genreNotIn?.contains(it.name) == true) SelectType.EXCLUDED
                else SelectType.NONE
            SelectFilterModel(selected = mutableStateOf(included), data = it.name)
        }

    fun updateFilter() {
        launch {
            mediaFilterDataDataStore.updateData { field.toMediaFilter() }
        }
    }
}