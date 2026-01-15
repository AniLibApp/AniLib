package com.revolgenx.anilib.list.ui.viewmodel

import android.content.ContentResolver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.data.exporter.MALExportService
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.MediaListFilterDataStore
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.isNotNull
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.entry.data.service.MediaListEntryService
import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.list.data.filter.MediaListCollectionFilter
import com.revolgenx.anilib.list.data.service.MediaListService
import com.revolgenx.anilib.list.data.sort.MediaListCollectionSortComparator
import com.revolgenx.anilib.list.ui.model.MediaListCollectionModel
import com.revolgenx.anilib.list.ui.model.MediaListModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.stream.Collectors.toList

abstract class MediaListViewModel(
    private val mediaListService: MediaListService,
    private val mediaListEntryService: MediaListEntryService,
    private val appPreferencesDataStore: AppPreferencesDataStore,
    private val mediaListDataStore: MediaListFilterDataStore,
    private val malExporter: MALExportService,
) :
    ResourceViewModel<MediaListCollectionModel, MediaListCollectionField>() {

    companion object {
        private const val searchSplitKeyword = "|#|,"
    }

    val searchHistory = mutableStateOf(emptyList<String>())
    val exportState = mutableStateOf<ResourceState<Boolean>?>(null)
    val exportMimeType = malExporter.mimeType

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
    val isLoggedInUserList get() = field.userId == loggedInUserId

    val openMediaListEntryEditor get() = appPreferencesDataStore.openMediaListEntryEditorOnClick
    val displayMode get() = appPreferencesDataStore.mediaListDisplayMode
    val otherDisplayMode get() = appPreferencesDataStore.otherMediaListDisplayMode

    var filter: MediaListCollectionFilter by mutableStateOf(MediaListCollectionFilter())
    var mediaListCollection = mutableStateListOf<MediaListModel>()
    var isFiltered by mutableStateOf(checkFiltered)
    private val checkFiltered
        get() = filter.run {
            sort != null
                    || year != null
                    || genre != null
                    || status != null
                    || formatsIn.isNullOrEmpty().not()
                    || isHentai != null
        }

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
        if (isLoggedInUserList) {
            filter = mediaListDataStore.get()
            isFiltered = checkFiltered
            launch {
                mediaListDataStore.data.collect {
                    if (filter != it) {
                        filter = it
                        isFiltered = checkFiltered
                        onFilterUpdate()
                    }
                }
            }
        }
    }

    override fun onComplete() {
        updateGroupNamesWithCount()
        onFilterUpdate()
    }

    override fun load(): Flow<MediaListCollectionModel?> {
        return mediaListService.getMediaListCollection(field)
    }

    private fun onFilterUpdate() {
        groupNamesWithCount.firstNotNullOfOrNull { m -> if (m.key == filter.groupName) m else null }
            ?.let { currentGroupNameWithCount = it.toPair() }
        filterData()
    }

    private fun filterData() {
        val mediaList =
            getData()?.lists?.firstOrNull { it.name == filter.groupName }
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
        val mediaListFilter = filter

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


    private fun updateGroupNamesWithCount() {
        val lists = getData()?.lists ?: return
        groupNamesWithCount = lists.associate { it.name.naText() to it.count }
    }

    fun updateCurrentGroupName(groupName: String) {
        if (isLoggedInUserList) {
            launch {
                mediaListDataStore.updateData {
                    it.copy(groupName = groupName)
                }
            }
        } else {
            filter = filter.copy(groupName = groupName)
            onFilterUpdate()
        }
    }

    fun updateFilter(filter: MediaListCollectionFilter) {
        if (isLoggedInUserList) {
            launch {
                mediaListDataStore.updateData {
                    filter
                }
            }
        } else {
            this@MediaListViewModel.filter = filter
            isFiltered = checkFiltered
            onFilterUpdate()
        }
    }

    fun increaseProgress(mediaList: MediaListModel) {
        mediaListEntryService.increaseProgress(mediaList)
            .onEach {
                it?.let { listModel ->
                    mediaList.progress = listModel.progress
                    mediaList.progressState?.value = listModel.progress
                    mediaList.status.value = listModel.status.value
                    mediaList.media?.mediaListEntry?.status?.value = listModel.status.value
                }
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
            history.removeAt(history.lastIndex)
        }
        launch {
            appPreferencesDataStore.mediaListSearchHistory.set(
                history.joinToString(
                    searchSplitKeyword
                )
            )
        }
    }

    fun getRandomMedia() = mediaListCollection.randomOrNull()

    fun export(
        uri: Uri,
        contentResolver: ContentResolver
    ) {
        if (!isSuccess) return

        launch {
            try {
                exportState.value = ResourceState.loading()
                withContext(Dispatchers.IO) {
                    contentResolver.openOutputStream(uri)?.use { outputStream ->
                        malExporter.export(mediaListCollection, outputStream)
                    } ?: throw Exception("Could not open output stream")
                }
                exportState.value = ResourceState.success(true)
            } catch (ex: Exception) {
                Timber.e(ex, "Failed to export to MAL")
                exportState.value = ResourceState.error()
            }
        }
    }
}