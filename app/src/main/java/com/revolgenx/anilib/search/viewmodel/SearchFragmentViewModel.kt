package com.revolgenx.anilib.search.viewmodel

import com.revolgenx.anilib.common.preference.loadRecentSearchField
import com.revolgenx.anilib.common.preference.saveRecentSearchField
import com.revolgenx.anilib.search.data.field.SearchField
import com.revolgenx.anilib.search.service.SearchService
import com.revolgenx.anilib.infrastructure.source.SearchSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel
import com.revolgenx.anilib.search.data.model.SearchFilterModel

class SearchFragmentViewModel(private val searchService: SearchService) :
    SourceViewModel<SearchSource, SearchField>() {
    override var field: SearchField = SearchField()
    var filterModel = SearchFilterModel()

    override fun createSource(): SearchSource {
        source = SearchSource(field, searchService, compositeDisposable)
        return source!!
    }

    fun applyFilter(){
        saveField()
        field.searchFilterModel = filterModel.copy()
    }

    fun saveField(){
        saveRecentSearchField(filterModel)
    }

    fun loadRecentField(){
        filterModel = loadRecentSearchField()
    }

}