package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.repository.util.Resource

class BrowseActivityViewModel : ViewModel() {
    var searchQuery: String = ""
    var searchLiveData = MutableLiveData<Resource<SearchData>>()
    var genreTagFields: MutableList<TagField> = mutableListOf()
    var tagTagFields: MutableList<TagField> = mutableListOf()
    var streamTagFields: MutableList<TagField> = mutableListOf()

    fun searchNow() {
        searchLiveData.value =
            Resource.success(SearchData(searchNow = true))
    }

    override fun onCleared() {
        genreTagFields.clear()
        tagTagFields.clear()
        streamTagFields.clear()
        super.onCleared()
    }

    companion object {
        data class SearchData(var searchNow: Boolean)
    }
}
