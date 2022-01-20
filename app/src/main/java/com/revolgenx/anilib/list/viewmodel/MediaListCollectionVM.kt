package com.revolgenx.anilib.list.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.list.data.field.MediaListCollectionFilterField
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class MediaListCollectionVM(
    app: Application,
    private val alMediaListCollectionService: MediaListCollectionService
) : BaseAndroidViewModel(app) {
    val field: MediaListCollectionField = MediaListCollectionField().also {
        it.userId = app.userId()
    }

    private val filter = MediaListCollectionFilterField()
    private val handler = Handler(Looper.getMainLooper())
    private val sortingComparator = MediaListCollectionSortingComparator()

    private var mediaListCollectionModel: MediaListCollectionModel? = null
    val filteredMediaListCollectionLiveData =
        MutableLiveData<Resource<FilteredMediaListGroupModel>>()

    val groupNamesWithCount = MutableLiveData<Map<String, Int>>()
    private var groupName = "All"


    var searchViewVisible = false
    var search: String = ""
        set(value) {
            if (field != value) {
                filter.search = value
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    filter()
                }, 500)
            }
            field = value
        }
        get() = filter.search

    var type: MediaType
        set(value) {
            this.field.type = value
        }
        get() = this.field.type

    fun getMediaList() {
        filteredMediaListCollectionLiveData.value = Resource.loading(null)
        alMediaListCollectionService.getMediaListCollection(field, compositeDisposable) {
            when (it.status) {
                Status.SUCCESS -> {
                    mediaListCollectionModel = it.data ?: return@getMediaListCollection

                    val lists = mediaListCollectionModel!!.lists!!
                    val groupNameMap = mutableMapOf<String, Int>()

                    groupNameMap["All"] = lists.first { it.name == "All" }.entries!!.count()

                    mediaListCollectionModel!!.user!!.mediaListOptions!!.let {
                        when (field.type) {
                            MediaType.ANIME -> {
                                it.animeList?.sectionOrder?.forEach { order ->
                                    lists.firstOrNull { it.name == order }?.let {
                                        groupNameMap[order] = it.entries!!.count()
                                    }
                                }
                            }
                            MediaType.MANGA -> {
                                it.mangaList?.sectionOrder?.forEach { order ->
                                    lists.firstOrNull { it.name == order }?.let {
                                        groupNameMap[order] = it.entries!!.count()
                                    }
                                }
                            }
                            else -> {
                            }
                        }
                    }

                    groupNamesWithCount.value = groupNameMap
                }
                Status.ERROR -> {
                    filteredMediaListCollectionLiveData.value = Resource.error(it.message)
                }
                else -> {
                }
            }
            filter()
        }
    }


    fun filter() {
        filteredMediaListCollectionLiveData.value = Resource.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val filteredList =
                        getFilteredList(
                            mediaListCollectionModel?.lists?.firstOrNull { it.name == groupName }?.entries
                                ?: emptyList()
                        )

                    FilteredMediaListGroupModel().also { model ->
                        model.entries = filteredList
                        model.user = mediaListCollectionModel!!.user
                        filteredMediaListCollectionLiveData.postValue(Resource.success(model))
                    }

                } catch (e: Exception) {
                    Timber.e(e)
                    launch(Dispatchers.Main) {
                        getApplication<Application>().makeToast(R.string.operation_failed)
                        filteredMediaListCollectionLiveData.postValue(
                            Resource.error(
                                e.message ?: "",
                                null,
                                e
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getFilteredList(listCollection: List<MediaListModel>): List<MediaListModel> {
        return if (filter.formatsIn.isEmpty()) listCollection else {
            listCollection.filter { filter.formatsIn.contains(it.media?.format) }
        }.let {
            if (filter.status == null) it else it.filter { it.status == filter.status }
        }.let {
            if (filter.genre == null) it else it.filter { it.media?.genres?.contains(filter.genre!!) == true }
        }.let {
            if (filter.search.isEmpty()) it else {
                it.filter { model ->
                    model.media?.title!!.romaji?.contains(filter.search, true) == true ||
                            model.media?.title!!.english?.contains(filter.search, true) == true ||
                            model.media?.title!!.native?.contains(filter.search, true) == true ||
                            model.media?.synonyms?.any { it.contains(filter.search, true) } == true
                }
            }
        }.let {
            if (filter.sort == null) it else {
                sortingComparator.type =
                    MediaListCollectionSortingComparator.MediaListSortingType.values()[filter.sort!!]
                it.sortedWith(sortingComparator)
            }
        }.toMutableList()
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}