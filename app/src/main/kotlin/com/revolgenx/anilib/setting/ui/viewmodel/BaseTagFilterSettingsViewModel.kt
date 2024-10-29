package com.revolgenx.anilib.setting.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.GenreCollectionDataStore
import com.revolgenx.anilib.common.data.store.MediaTagCollectionDataStore
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.media.ui.model.MediaGenreModel
import com.revolgenx.anilib.media.ui.model.MediaTagModel
import com.revolgenx.anilib.setting.ui.model.MediaTagFilterModel

abstract class BaseTagFilterSettingsViewModel() : ViewModel() {
    val excludedAnimeTagCollection = mutableListOf<MediaTagFilterModel>()
    val excludedMangaTagCollection = mutableListOf<MediaTagFilterModel>()

    val excludedAnimeTags = mutableStateOf<List<MediaTagFilterModel>>(emptyList())
    val excludedMangaTags = mutableStateOf<List<MediaTagFilterModel>>(emptyList())


    fun addExcludedTag(tag: MediaTagFilterModel, isAnime: Boolean) {
        if (isAnime) {
            excludedAnimeTags.value += tag
        } else {
            excludedMangaTags.value += tag
        }
    }

    fun removeExcludedTag(tag: MediaTagFilterModel, isAnime: Boolean) {
        if (isAnime) {
            excludedAnimeTags.value -= tag
        } else {
            excludedMangaTags.value -= tag
        }
    }

    abstract fun saveExcludedTags(isAnime: Boolean)
}

class TagFilterSettingsViewModel(
    private val mediaTagCollectionDataStore: MediaTagCollectionDataStore,
    appPreferencesDataStore: AppPreferencesDataStore
) : BaseTagFilterSettingsViewModel() {
    private var mediaTagCollection = mediaTagCollectionDataStore.data.get().tags
    private val canShowAdultContent = appPreferencesDataStore.displayAdultContent.get()!!

    init {
        mediaTagCollection.forEach {
            if (it.isAdult && !canShowAdultContent) {
                return@forEach
            }
            excludedAnimeTagCollection += MediaTagFilterModel(
                name = it.name,
                isSelected = mutableStateOf(it.isExcludedInAnime),
                data = it
            )
            excludedMangaTagCollection += MediaTagFilterModel(
                name = it.name,
                isSelected = mutableStateOf(it.isExcludedInManga),
                data = it
            )
        }

        excludedAnimeTags.value = excludedAnimeTagCollection.filter { it.isSelected.value }
        excludedMangaTags.value = excludedMangaTagCollection.filter { it.isSelected.value }
    }

    override fun saveExcludedTags(isAnime: Boolean) {
        mediaTagCollection = if (isAnime) {
            excludedAnimeTagCollection.map {
                (it.data as MediaTagModel).copy(isExcludedInAnime = it.isSelected.value)
            }
        } else {
            excludedMangaTagCollection.map {
                (it.data as MediaTagModel).copy(isExcludedInManga = it.isSelected.value)
            }
        }


        launch {
            mediaTagCollectionDataStore.updateData {
                it.copy(tags = mediaTagCollection)
            }
        }
    }
}

class GenreFilterSettingsViewModel(
    private val mediaGenreCollectionDataStore: GenreCollectionDataStore,
    appPreferencesDataStore: AppPreferencesDataStore
) : BaseTagFilterSettingsViewModel() {
    private var mediaGenreCollection = mediaGenreCollectionDataStore.data.get().genre
    private val canShowAdultContent = appPreferencesDataStore.displayAdultContent.get()!!

    init {
        mediaGenreCollection.forEach {
            if (it.name.contains("Hentai", ignoreCase = true) && !canShowAdultContent) {
                return@forEach
            }
            excludedAnimeTagCollection += MediaTagFilterModel(
                name = it.name,
                isSelected = mutableStateOf(it.isExcludedInAnime),
                data = it
            )
            excludedMangaTagCollection += MediaTagFilterModel(
                name = it.name,
                isSelected = mutableStateOf(it.isExcludedInManga),
                data = it
            )
        }

        excludedAnimeTags.value = excludedAnimeTagCollection.filter { it.isSelected.value }
        excludedMangaTags.value = excludedMangaTagCollection.filter { it.isSelected.value }
    }

    override fun saveExcludedTags(isAnime: Boolean) {
        mediaGenreCollection = if (isAnime) {
            excludedAnimeTagCollection.map {
                (it.data as MediaGenreModel).copy(isExcludedInAnime = it.isSelected.value)
            }
        } else {
            excludedMangaTagCollection.map {
                (it.data as MediaGenreModel).copy(isExcludedInManga = it.isSelected.value)
            }
        }


        launch {
            mediaGenreCollectionDataStore.updateData {
                it.copy(genre = mediaGenreCollection)
            }
        }
    }
}

