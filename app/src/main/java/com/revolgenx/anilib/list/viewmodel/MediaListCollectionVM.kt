package com.revolgenx.anilib.list.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.common.preference.animeListStatusHistory
import com.revolgenx.anilib.common.preference.loadMediaListCollectionFilter
import com.revolgenx.anilib.common.preference.mangaListStatusHistory
import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.list.data.meta.MediaListCollectionFilterMeta
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.list.data.sorting.MediaListCollectionSortingComparator
import com.revolgenx.anilib.list.service.MediaListCollectionService
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.common.viewmodel.BaseAndroidViewModel
import com.revolgenx.anilib.list.data.model.FilteredMediaListGroupModel
import com.revolgenx.anilib.list.data.model.MediaListCollectionModel
import com.revolgenx.anilib.list.source.MediaListCollectionSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class MediaListCollectionVM(
    app: Application,
    private val alMediaListCollectionService: MediaListCollectionService,
    private val mediaListCollectionStore: MediaListCollectionStoreVM
) : BaseAndroidViewModel(app) {

    val field: MediaListCollectionField = MediaListCollectionField().also {
        it.userId = UserPreference.userId
    }

    private val isLoggedInUser get() = field.userId == UserPreference.userId

    val mediaListFilter by lazy {
        if (isLoggedInUser) {
            loadMediaListCollectionFilter(app, type.ordinal)
        } else {
            MediaListCollectionFilterMeta()
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private val sortingComparator = MediaListCollectionSortingComparator()

    private var mediaListCollectionModel: MediaListCollectionModel? = null

    var filteredMediaListCollection: FilteredMediaListGroupModel? = null

    val groupNamesWithCount = MutableLiveData<Map<String, Int>>()

    val sourceLiveData = MutableLiveData<MediaListCollectionSource>()

    private val loadingSource = MediaListCollectionSource(Resource.loading(null))

    val currentGroupNameHistory
        get() = if (isLoggedInUser) {
            if (field.type == MediaType.ANIME)
                animeListStatusHistory(getApplication())
            else
                mangaListStatusHistory(getApplication())
        } else {
            groupNameHistory
        }

    var groupNameHistory = "All"

    var searchViewVisible = false
    var search: String = ""
        set(value) {
            if (field != value) {
                mediaListFilter.search = value
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    filter()
                }, 500)
            }
            field = value
        }
        get() = mediaListFilter.search

    var type: MediaType
        set(value) {
            this.field.type = value
        }
        get() = this.field.type

    fun getMediaList() {
        sourceLiveData.value = loadingSource
        alMediaListCollectionService.getMediaListCollection(field, compositeDisposable) {
            when (it.status) {
                Status.SUCCESS -> {
                    mediaListCollectionModel = it.data ?: return@getMediaListCollection
                    if(isLoggedInUser){
                        mediaListCollectionStore.lists = mediaListCollectionModel?.lists ?: mutableListOf()
                    }
                    reevaluateGroupNameWithCount()
                }
                Status.ERROR -> {
                    sourceLiveData.value = MediaListCollectionSource(Resource.error(it.message))
                }
                else -> {
                }
            }
            filter()
        }
    }

    fun reevaluateGroupNameWithCount(){
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
                                animeListStatusHistory(getApplication(), "All")
                            } else {
                                mangaListStatusHistory(getApplication(), "All")
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

                    filteredMediaListCollection = FilteredMediaListGroupModel().also { model ->
                        model.entries = filteredList
                        model.user = mediaListCollectionModel!!.user
                    }
                    sourceLiveData.postValue(MediaListCollectionSource(Resource.success(filteredList)))
                } catch (e: Exception) {
                    Timber.e(e)
                    launch(Dispatchers.Main) {
                        getApplication<Application>().makeToast(R.string.operation_failed)
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
            if (mediaListFilter.search.isEmpty()) it else {
                it.filter { model ->
                    model.media?.title!!.romaji?.contains(mediaListFilter.search, true) == true ||
                            model.media?.title!!.english?.contains(
                                mediaListFilter.search,
                                true
                            ) == true ||
                            model.media?.title!!.native?.contains(
                                mediaListFilter.search,
                                true
                            ) == true ||
                            model.media?.synonyms?.any {
                                it.contains(
                                    mediaListFilter.search,
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


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}