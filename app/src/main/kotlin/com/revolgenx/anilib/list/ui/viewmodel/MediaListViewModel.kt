package com.revolgenx.anilib.list.ui.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.MediaListFilterDataStore
import com.revolgenx.anilib.common.ext.isNotNull
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.entry.data.field.SaveMediaListEntryField
import com.revolgenx.anilib.entry.data.service.MediaListEntryService
import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.list.data.filter.MediaListCollectionFilter
import com.revolgenx.anilib.list.data.service.MediaListService
import com.revolgenx.anilib.list.data.sort.MediaListCollectionSortComparator
import com.revolgenx.anilib.list.ui.model.MediaListCollectionModel
import com.revolgenx.anilib.list.ui.model.MediaListModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.stream.Collectors.toList

abstract class MediaListViewModel(
    private val mediaListService: MediaListService,
    private val mediaListEntryService: MediaListEntryService,
    private val appPreferencesDataStore: AppPreferencesDataStore,
    private val mediaListDataStore: MediaListFilterDataStore,
) :
    ResourceViewModel<MediaListCollectionModel, MediaListCollectionField>() {


    companion object {
        private const val searchSplitKeyword = "|#|,"
    }

    val searchHistory = mutableStateOf(emptyList<String>())

    init {
        launch {
            appPreferencesDataStore.mediaListSearchHistory.collect {
                searchHistory.value =
                    it!!.takeIf { it.isNotBlank() }?.split(searchSplitKeyword) ?: emptyList()
            }
            appPreferencesDataStore.mediaTitleType.collect {
                sortingComparator.titleType = it!!
            }
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private val sortingComparator =
        MediaListCollectionSortComparator(appPreferencesDataStore.mediaTitleType.get()!!)

    private var loggedInUserId = appPreferencesDataStore.userId.get()
    private val isLoggedInUser get() = field.userId == loggedInUserId

    val openMediaListEntryEditor get() = appPreferencesDataStore.openMediaListEntryEditorOnClick
    val displayMode get() = appPreferencesDataStore.mediaListDisplayMode

    var filter: MediaListCollectionFilter? by mutableStateOf(null)
    var mediaListCollection = mutableStateListOf<MediaListModel>()


    var groupNamesWithCount by mutableStateOf(mapOf("All" to 0))
    val currentGroupName by derivedStateOf { currentGroupNameWithCount.let { "${it.first} ${it.second}" } }
    var currentGroupNameWithCount by mutableStateOf("All" to 0)


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
        if (isLoggedInUser) {
            launch {
                mediaListDataStore.data.collect {
                    filter = it
                    onFilterUpdate()
                }
            }
        } else {
            filter = MediaListCollectionFilter()
            onFilterUpdate()
        }
    }


    override fun onComplete() {
        getGroupNameWithCount()
        filterData()
    }

    override fun load(): Flow<MediaListCollectionModel?> {
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

    private fun filterData() {
        filter ?: return
        val mediaList =
            getData()?.lists?.firstOrNull { it.name == currentGroupNameWithCount.first }
                ?: let {
                    if (currentGroupNameWithCount.first != "All") {
                        launch {
                            mediaListDataStore.updateData {
                                it.copy(groupName = "All")
                            }
                        }
                    }
                    null
                }
        val mediaListEntries = mediaList?.entries.orEmpty()
        val filteredList = getFilteredList(mediaListEntries)
        mediaListCollection.clear()
        mediaListCollection.addAll(filteredList)
    }

    private fun getFilteredList(listCollection: List<MediaListModel>): List<MediaListModel> {
        val mediaListFilter = filter ?: return emptyList()

        return listCollection.parallelStream()
            .filter { model ->
                val media = model.media ?: return@filter false
                (mediaListFilter.formatsIn.takeIf { !it.isNullOrEmpty() }?.contains(media.format)
                    ?: true) &&
                        (mediaListFilter.status.takeIf { it.isNotNull() }?.equals(media.status)
                            ?: true) &&
                        (mediaListFilter.genre.takeIf { it.isNotNull() }
                            ?.let { media.genres?.contains(it) } ?: true) &&
                        (mediaListFilter.isHentai.takeIf { it.isNotNull() }?.equals(media.isAdult)
                            ?: true) &&
                        (query.takeIf { it.isNotEmpty() }?.let { query ->
                            media.title!!.romaji?.contains(query, true) == true ||
                                    media.title.english?.contains(
                                        query,
                                        true
                                    ) == true ||
                                    media.title.native?.contains(
                                        query,
                                        true
                                    ) == true ||
                                    media.synonyms?.any {
                                        it.contains(
                                            query,
                                            true
                                        )
                                    } == true
                        } ?: true)
            }.let {
                if (mediaListFilter.sort == null) it else {
                    sortingComparator.type = mediaListFilter.sort
                    it.sorted(sortingComparator)
                }
            }.collect(toList())
    }


    private fun getGroupNameWithCount() {
        val lists = getData()?.lists ?: return
        val groupNameMap = lists.associate { it.name.naText() to it.count }
        groupNamesWithCount = groupNameMap
        updateCurrentGroupNameWithCount()
    }

    fun updateCurrentGroupName(groupName: String) {
        filter ?: return
        if (isLoggedInUser) {
            launch {
                mediaListDataStore.updateData {
                    it.copy(groupName = groupName)
                }
            }
        } else {
            filter = filter?.copy(groupName = groupName)
            onFilterUpdate()
        }
    }

    fun updateFilter(filter: MediaListCollectionFilter) {
        if (isLoggedInUser) {
            launch {
                mediaListDataStore.updateData {
                    filter
                }
            }
        } else {
            this@MediaListViewModel.filter = filter
            onFilterUpdate()
        }
    }

    fun increaseProgress(mediaList: MediaListModel) {
        val oldProgress = mediaList.progress
        val newProgress = (oldProgress ?: 0) + 1
        val episodesOrChapters = mediaList.media?.totalEpisodesOrChapters

        if (episodesOrChapters != null && newProgress > episodesOrChapters) return

        val progressSaveField = SaveMediaListEntryField().also {
            it.id = mediaList.id
            it.progress = newProgress
        }
        mediaListEntryService.saveMediaListEntry(progressSaveField)
            .onEach {
                mediaList.progress = it?.progress
                mediaList.progressState?.value = it?.progress
            }.catch {
                saveFailed(it)
            }.launchIn(viewModelScope)
    }

    fun deleteSearchHistory(value: String) {
        val history = searchHistory.value.toMutableList()
        history.remove(value)
        launch {
            appPreferencesDataStore.mediaListSearchHistory.set(
                history.joinToString(
                    searchSplitKeyword
                )
            )
        }
    }

    fun updateSearchHistory() {
        val search = search.takeIf { it.isNotBlank() } ?: return
        val history = searchHistory.value.toMutableList()
        history.remove(search)
        history.add(0, search)
        if (history.size > 10) {
            history.removeLast()
        }
        launch {
            appPreferencesDataStore.mediaListSearchHistory.set(
                history.joinToString(
                    searchSplitKeyword
                )
            )
        }
    }

}