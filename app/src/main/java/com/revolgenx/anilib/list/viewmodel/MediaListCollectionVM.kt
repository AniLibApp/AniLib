package com.revolgenx.anilib.list.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.list.data.meta.MediaListCollectionFilterMeta
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.list.data.sorting.MediaListCollectionSortingComparator
import com.revolgenx.anilib.list.service.MediaListCollectionService
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.common.viewmodel.BaseViewModel
import com.revolgenx.anilib.entry.service.MediaListEntryService
import com.revolgenx.anilib.entry.service.increaseProgress
import com.revolgenx.anilib.list.data.model.MediaListCollectionModel
import com.revolgenx.anilib.list.source.MediaListCollectionSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class MediaListCollectionVM(
    private val alMediaListCollectionService: MediaListCollectionService,
    private val mediaListEntryService: MediaListEntryService,
    private val mediaListCollectionStore: MediaListCollectionStoreVM
) : BaseViewModel() {

    val field: MediaListCollectionField = MediaListCollectionField()

    private val isLoggedInUser get() = field.userId == UserPreference.userId

    val mediaListFilter by lazy {
        if (isLoggedInUser) {
            loadMediaListCollectionFilter(type.ordinal)
        } else {
            MediaListCollectionFilterMeta()
        }.also {
            it.type = type.ordinal
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private val sortingComparator = MediaListCollectionSortingComparator()

    private var mediaListCollectionModel: MediaListCollectionModel? = null

    val groupNamesWithCount = MutableLiveData<Map<String, Int>>()

    val sourceLiveData = MutableLiveData<MediaListCollectionSource>()
    val collectionSource get() = sourceLiveData.value

    private val loadingSource = MediaListCollectionSource(Resource.loading(null))

    val currentGroupNameHistory
        get() = if (isLoggedInUser) {
            if (field.type == MediaType.ANIME)
                animeListStatusHistory()
            else
                mangaListStatusHistory()
        } else {
            groupNameHistory
        }

    var groupNameHistory = "All"

    var searchViewVisible = false

    var query = ""
    var search: String = ""
        set(value) {
            if (field != value) {
                query = value
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    filter()
                }, 500)
            }
            field = value
        }
        get() = query

    var type: MediaType
        set(value) {
            this.field.type = value
        }
        get() = this.field.type

    fun getMediaList() {
        sourceLiveData.value = loadingSource
        alMediaListCollectionService.getMediaListCollection(field, compositeDisposable) {
            when (it) {
                is Resource.Success -> {
                    mediaListCollectionModel = it.data ?: return@getMediaListCollection
                    if (isLoggedInUser) {
                        mediaListCollectionStore.lists =
                            mediaListCollectionModel?.lists ?: mutableListOf()
                    }
                    reEvaluateGroupNameWithCount()
                    filter()
                }
                is Resource.Error -> {
                    sourceLiveData.value = MediaListCollectionSource(Resource.error(it.message))
                }
                else -> {
                }
            }
        }
    }

    fun reEvaluateGroupNameWithCount() {
        val lists = mediaListCollectionModel?.lists ?: return
        val groupNameMap = mutableMapOf<String, Int>()
        groupNameMap["All"] = lists.first { it.name == "All" }.entries!!.count()
        mediaListCollectionModel!!.user!!.mediaListOptions!!.let {
            when (field.type) {
                MediaType.ANIME -> {
                    it.animeList?.sectionOrder?.forEach { order ->
                        lists.firstOrNull { it.name == order }?.let {
                            groupNameMap[order] = it.entries?.count() ?: 0
                        }
                    }
                }
                MediaType.MANGA -> {
                    it.mangaList?.sectionOrder?.forEach { order ->
                        lists.firstOrNull { it.name == order }?.let {
                            groupNameMap[order] = it.entries?.count() ?: 0
                        }
                    }
                }
                else -> {
                }
            }
        }
        groupNamesWithCount.value = groupNameMap
    }

    fun applyFilter(){
        if(isLoggedInUser){
            storeMediaListFilterField(mediaListFilter)
        }
        filter()
    }

    fun filter() {
        sourceLiveData.value = loadingSource
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    if (isLoggedInUser) {
                        val hasRecentGroupNameHistory =
                            mediaListCollectionModel?.lists?.any { it.name == currentGroupNameHistory } == true
                        if (!hasRecentGroupNameHistory) {
                            if (field.type == MediaType.ANIME) {
                                animeListStatusHistory("All")
                            } else {
                                mangaListStatusHistory("All")
                            }
                        }
                    }


                    val mediaListEntries =
                        mediaListCollectionModel
                            ?.lists
                            ?.firstOrNull { it.name == currentGroupNameHistory }
                            ?.entries
                            ?: emptyList()

                    val filteredList = getFilteredList(mediaListEntries)
                    sourceLiveData.postValue(MediaListCollectionSource(Resource.success(filteredList)))
                } catch (e: Exception) {
                    Timber.e(e)
                    launch(Dispatchers.Main) {
                        sourceLiveData.postValue(
                            MediaListCollectionSource(
                                Resource.error(
                                    e.message ?: "",
                                    null,
                                    e
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    fun increaseProgress(item: MediaListModel) {
        mediaListEntryService.increaseProgress(item, compositeDisposable)
    }

    private fun getFilteredList(listCollection: List<MediaListModel>): List<MediaListModel> {
        return if (mediaListFilter.formatsIn.isNullOrEmpty()) listCollection else {
            listCollection.filter { mediaListFilter.formatsIn!!.contains(it.media?.format) }
        }.let {
            if (mediaListFilter.status == null) it else it.filter { it.media?.status == mediaListFilter.status }
        }.let {
            if (mediaListFilter.genre == null) it else it.filter {
                it.media?.genres?.contains(
                    mediaListFilter.genre!!
                ) == true
            }
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
                sortingComparator.type =
                    MediaListCollectionSortingComparator.MediaListSortingType.values()[mediaListFilter.sort!!]
                it.sortedWith(sortingComparator)
            }
        }.toMutableList()
    }
}