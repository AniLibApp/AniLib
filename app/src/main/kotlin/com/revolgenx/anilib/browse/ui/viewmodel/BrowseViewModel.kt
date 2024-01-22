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
import com.revolgenx.anilib.browse.data.store.BrowsePreferencesDataStore
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel

class BrowseViewModel(
    private val browseService: BrowseService,
    private val browsePreferencesDataStore: BrowsePreferencesDataStore
) :
    PagingViewModel<BaseModel, BrowseField, BrowsePagingSource>() {

    private val handler = Handler(Looper.getMainLooper())
    override var field: BrowseField = BrowseField()

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
            browsePreferencesDataStore.browseHistory.collect {
                searchHistory.value =
                    it!!.takeIf { it.isNotBlank() }?.split(searchSplitKeyword) ?: emptyList()
            }
        }
    }

    fun search(){
        handler.removeCallbacksAndMessages(null)
        refresh()
    }

    fun updateSearchHistory() {
        val search = field.search?.takeIf { it.isNotBlank() } ?: return
        val history = searchHistory.value.toMutableList()
        history.remove(search)
        history.add(0, search)
        if (history.size > 10) {
            history.removeLast()
        }
        launch {
            browsePreferencesDataStore.browseHistory.set(history.joinToString(searchSplitKeyword))
        }
    }

    fun deleteSearchHistory(value: String) {
        val history = searchHistory.value.toMutableList()
        history.remove(value)
        launch {
            browsePreferencesDataStore.browseHistory.set(history.joinToString(searchSplitKeyword))
        }
    }

    fun updateFromBrowseFilterData(browseFilterData: BrowseFilterData){
        this.field = browseFilterData.toBrowseField()
    }
}