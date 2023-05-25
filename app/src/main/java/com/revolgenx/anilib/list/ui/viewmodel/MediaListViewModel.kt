package com.revolgenx.anilib.list.ui.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.revolgenx.anilib.common.data.store.AppDataStore
import com.revolgenx.anilib.common.data.store.MediaListFilterDataStore
import com.revolgenx.anilib.common.data.store.runUserId
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ext.launchIO
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.list.data.filter.MediaListCollectionFilter
import com.revolgenx.anilib.list.data.service.MediaListService
import com.revolgenx.anilib.list.data.sort.MediaListCollectionSortComparator
import com.revolgenx.anilib.list.ui.model.MediaListCollectionModel
import com.revolgenx.anilib.list.ui.model.MediaListModel
import kotlinx.coroutines.flow.Flow

abstract class MediaListViewModel(
    private val mediaListService: MediaListService,
    private val appDataStore: AppDataStore,
    private val mediaListDataStore: MediaListFilterDataStore
) :
    ResourceViewModel<MediaListCollectionModel, MediaListCollectionField>() {

    private val loggedInUserId = appDataStore.runUserId()

    private val handler = Handler(Looper.getMainLooper())
    private val sortingComparator = MediaListCollectionSortComparator()
    private val isLoggedInUser get() = loggedInUserId == field.userId

    var filter: MediaListCollectionFilter? by mutableStateOf(null)
    var mediaListCollection = mutableStateListOf<MediaListModel>()


    var groupNamesWithCount by mutableStateOf(mapOf("All" to 0))
    val currentGroupName by derivedStateOf { currentGroupNameWithCount.let { "${it.first} ${it.second}" } }
    var currentGroupNameWithCount by mutableStateOf("All" to 0)

    var searchHistory: List<String> = emptyList()

    var query by mutableStateOf("")
    var search: String = ""
        set(value) {
            if (field != value) {
                query = value
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    filterData()
                }, 700)
            }
            field = value
        }
        get() = query

    fun searchNow() {
        handler.removeCallbacksAndMessages(null)
        filterData()
    }

    override fun onInit() {
        launch {
            if (isLoggedInUser) {
                mediaListDataStore.data.collect {
                    filter = it
                    onFilterUpdate()
                }
            } else {
                filter = MediaListCollectionFilter()
                onFilterUpdate()
            }
        }
    }


    override fun onComplete() {
        getGroupNameWithCount()
        filterData()
    }

    override fun loadData(): Flow<MediaListCollectionModel?> {
        return mediaListService.getMediaListCollection(field)
    }

    private fun updateCurrentGroupNameWithCount() {
        groupNamesWithCount.getOrDefault(currentGroupNameWithCount.first, null)?.let {
            currentGroupNameWithCount = currentGroupNameWithCount.first to it
        }
    }

    private fun onFilterUpdate() {
        filter?.groupName?.let { name ->
            groupNamesWithCount.firstNotNullOfOrNull { m -> if (m.key == name) m else null }
                ?.let { currentGroupNameWithCount = it.toPair() }
        }
        updateCurrentGroupNameWithCount()
        filterData()
    }

    fun filterData() {
        filter ?: return
        launchIO {
            val mediaList =
                getData()?.lists?.firstOrNull { it.name == currentGroupNameWithCount.first }
                    ?: let {
                        if (currentGroupNameWithCount.first != "All") {
                            mediaListDataStore.updateData {
                                it.copy(groupName = "All")
                            }
                        }
                        null
                    }
            val mediaListEntries = mediaList?.entries ?: emptyList()
            val filteredList = getFilteredList(mediaListEntries)
            mediaListCollection.clear()
            mediaListCollection.addAll(filteredList)
        }
    }

    private fun getFilteredList(listCollection: List<MediaListModel>): List<MediaListModel> {
        val mediaListFilter = filter ?: return emptyList()

        return if (mediaListFilter.formatsIn.isNullOrEmpty()) listCollection else {
            listCollection.filter { mediaListFilter.formatsIn.contains(it.media?.format) }
        }.let {
            if (mediaListFilter.status == null) it else it.filter { it.media?.status == mediaListFilter.status }
        }.let {
            if (mediaListFilter.genre == null) it else it.filter {
                it.media?.genres?.contains(
                    mediaListFilter.genre
                ) == true
            }
        }.let {
            if (mediaListFilter.isHentai == null) it else it.filter { it.media?.isAdult == mediaListFilter.isHentai }
        }.let {
            if (query.isEmpty()) it else {
                it.filter { model ->
                    model.media?.title!!.romaji?.contains(query, true) == true ||
                            model.media?.title!!.english?.contains(
                                query,
                                true
                            ) == true ||
                            model.media?.title!!.native?.contains(
                                query,
                                true
                            ) == true ||
                            model.media?.synonyms?.any {
                                it.contains(
                                    query,
                                    true
                                )
                            } == true
                }
            }
        }.let {
            if (mediaListFilter.sort == null) it else {
                sortingComparator.type = mediaListFilter.sort
                it.sortedWith(sortingComparator)
            }
        }
    }


    private fun getGroupNameWithCount() {
        val lists = getData()?.lists ?: return
        val groupNameMap = lists.associate { it.name.naText() to it.count }
        groupNamesWithCount = groupNameMap
        updateCurrentGroupNameWithCount()
    }

    fun updateCurrentGroupName(groupName: String) {
        filter ?: return
        launch {
            if (isLoggedInUser) {
                mediaListDataStore.updateData {
                    it.copy(groupName = groupName)
                }
            } else {
                filter = filter?.copy(groupName = groupName)
                onFilterUpdate()
            }
        }
    }

    fun updateFilter(filter: MediaListCollectionFilter) {
        launch {
            if (isLoggedInUser) {
                mediaListDataStore.updateData {
                    filter
                }
            } else {
                this@MediaListViewModel.filter = filter
                onFilterUpdate()
            }
        }
    }
}