package com.revolgenx.anilib.search.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.common.data.field.TagField
import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.search.data.field.SearchField
import com.revolgenx.anilib.search.service.SearchService
import com.revolgenx.anilib.infrastructure.source.SearchSource
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

class SearchFragmentViewModel(private val searchService: SearchService) :
    SourceViewModel<SearchSource, SearchField>() {
    override var field: SearchField = SearchField()
    private val handler = Handler(Looper.getMainLooper())
    private val searchLiveData = MutableLiveData<List<BaseModel>>()

    override fun createSource(): SearchSource {
        source = SearchSource(field, searchService, compositeDisposable)
        return source!!
    }

}