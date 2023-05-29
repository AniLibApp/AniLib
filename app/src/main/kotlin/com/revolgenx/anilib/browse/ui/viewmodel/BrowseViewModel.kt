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
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel

class BrowseViewModel(private val browseService: BrowseService) :
    PagingViewModel<BaseModel, BrowseField, BrowsePagingSource>() {

    private val handler = Handler(Looper.getMainLooper())
    override val field: BrowseField = BrowseField()

    val listType =
        derivedStateOf { if (field.browseType.value == BrowseTypes.STUDIO) ListPagingListType.COLUMN else ListPagingListType.GRID }
    override val pagingSource: BrowsePagingSource
        get() = BrowsePagingSource(this.field, browseService)

    var query by mutableStateOf("")
    var search: String = ""
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

    var searchHistory by mutableStateOf("")

}